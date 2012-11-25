%% Author: Milind
%% Created: Nov 22, 2012
%% Description: TODO: Add description to useless
-module(mygossip).
-compile([debug_info, export_all]).

floor(X) ->
    T = erlang:trunc(X),
    case (X - T) of
        Neg when Neg < 0 -> T - 1;
        Pos when Pos > 0 -> T;
        _ -> T
    end.

ceiling(X) ->
    T = erlang:trunc(X),
    case (X - T) of
        Neg when Neg < 0 -> T;
        Pos when Pos > 0 -> T + 1;
        _ -> T
    end.

myGossip(Fragment_Id, Data_Values , Neighbours_List, Delay , KCount , MIN,MAX,AVERAGE,MEDIAN,Initialized,Infected,IsRunning) -> 
	%io:format("~p Invoked ~n",[self()]),
	if 
		IsRunning == 1 ->
			%io:format("~p Running ~n",[self()]),
			if 
				Initialized == 0 ->
				   io:format("~p Not initialized yet ~n",[self()]);
				Initialized == 1 ->
					%io:format("~p Data has been initialized ~n",[self()]),
					%% Find a random neighbour from neighbour list
					%timer:sleep(Delay),
					Neighbours_List_2 = lists:delete(self(),Neighbours_List),
					NewNodePID = lists:nth(random:uniform(length(Neighbours_List)), Neighbours_List) , %%TODO is this index finding correct?
					if 
						self() == NewNodePID ->
							%io:format("~p chose itself, just sleep",[self()]),
							timer:sleep(Delay),
							myGossip(Fragment_Id, Data_Values , Neighbours_List, Delay , KCount , MIN,MAX,AVERAGE,MEDIAN,Initialized,Infected,IsRunning);
						true ->
							if 
								Infected == 0 ->
						   			%% TODO do a PULL here
						   			io:format("~p PULL from ~p~n",[self(),NewNodePID]),
									NewNodePID ! {pull_request , self()};
					  			Infected == 1 ->
						   		if 
							   		KCount >= 1 ->
						   				%% TODO do a PUSH here
										if 
											AVERAGE == 0 -> 
												NEW_TOTAL_SUM = lists:sum(Data_Values),
												NEW_TOTAL_NUM = length(Data_Values);
											true ->
												NEW_TOTAL_SUM = AVERAGE,
												NEW_TOTAL_NUM = MEDIAN
										end,
						   				%io:format("~p PUSH to ~p~n",[self(),NewNodePID]),
										NewNodePID ! {push_average_request,self(),Data_Values,NEW_TOTAL_SUM,NEW_TOTAL_NUM, MIN, MAX};
							   		KCount < 1 ->
										io:format("~p FINAL AVERAGE ~p MIN ~p MAX ~p MEDIAN ~p~n",[self(),AVERAGE/MEDIAN , MIN ,MAX, MEDIAN])
								end
							end
					end				   
			end;
	 	IsRunning == 0 ->
			io:format("~p Not Running yet.~n",[self()])
	end,
	   
	%% if not initialized then wait for the initialize message
	%% now that PUSH or PULL message has been sent , wait for a response and then updated your state.
	%% NOTE: PULL's will have a response of no_infection if it contacts an uninfected node, and gets a proper message if contacts infected. After that, a PULL message will never be sent by the node.
	%% NOTE: PUSH will start ONLY after a node has been infected.
	receive
		{initialize, Fragment_Id_in, Data_Values_in , Neighbours_List_in, Delay_in , KCount_in , MIN_in,MAX_in,AVERAGE_in,MEDIAN_in} ->
			IN_MIN = lists:min(Data_Values_in),
			IN_MAX = lists:max(Data_Values_in),
			io:format("~p received initialize [id,values,KCount, MIN, MAX , neighbour] ~p:~p:~p:~p:~p: ~n", [self(), Fragment_Id_in, Data_Values_in,KCount_in,IN_MIN,IN_MAX]),
			myGossip(Fragment_Id_in, Data_Values_in , Neighbours_List_in, Delay_in , KCount_in , IN_MIN ,IN_MAX,AVERAGE_in,MEDIAN_in,1,0,0);
		{initialize_average} ->
			io:format("~p received initialized_average message.~n",[self()]),
			myGossip(Fragment_Id, Data_Values , Neighbours_List, Delay , KCount ,MIN,MAX,AVERAGE,MEDIAN,1,1,1);
		{push_average_request,Pid,Source_Data,TOTAL_SUM,TOTAL_NUMBERS, MIN_MESSAGE , MAX_MESSAGE} ->
			if
				MIN_MESSAGE < MIN ->
					NEW_MIN = MIN_MESSAGE;
				MIN_MESSAGE >= MIN ->
					NEW_MIN = MIN
			end,
			if
				MAX_MESSAGE >= MAX ->
					NEW_MAX = MAX_MESSAGE;
				MAX_MESSAGE < MAX ->
					NEW_MAX = MAX
			end,
			
			Source_Sum=lists:sum(Source_Data),
			Source_length = length(Source_Data),
			My_Sum = lists:sum(Data_Values),
			My_length = length(Data_Values),
			Current_Average = AVERAGE,
			Computed_Average = (My_Sum+TOTAL_SUM)/(TOTAL_NUMBERS+My_length),
			%io:format("~p push_average_request ~p , source_sum ~p my_sum ~p , computed average ~p ~n",[self(),Pid,Source_Sum,My_Sum,Computed_Average]),
			Pid ! { response_push_average_request,self(), My_Sum,My_Sum+TOTAL_SUM,TOTAL_NUMBERS+My_length, NEW_MIN , NEW_MAX}, %can return either my sum or computed average
			myGossip(Fragment_Id, Data_Values , Neighbours_List, Delay , KCount , MIN_MESSAGE ,MAX_MESSAGE ,My_Sum+TOTAL_SUM,TOTAL_NUMBERS+My_length,1,1,1);
		{response_push_average_request, Pid , Response_Sum,TOTAL_SUM,TOTAL_NUMBERS , MIN_MESSAGE , MAX_MESSAGE} ->
			if
				MIN_MESSAGE < MIN ->
					NEW_MIN = MIN_MESSAGE;
				MIN_MESSAGE >= MIN ->
					NEW_MIN = MIN
			end,
			if
				MAX_MESSAGE >= MAX ->
					NEW_MAX = MAX_MESSAGE;
				MAX_MESSAGE < MAX ->
					NEW_MAX = MAX
			end,
			My_Sum = lists:sum(Data_Values),
			My_length = length(Data_Values),
			Computed_Average = (TOTAL_SUM)/TOTAL_NUMBERS,
			%io:format("~p response_push_average_request ~p , Response_sum ~p my_sum ~p computed average ~p ~n",[self(),Pid,Response_Sum,My_Sum,Computed_Average]),
			myGossip(Fragment_Id, Data_Values , Neighbours_List, Delay , KCount-1 , NEW_MIN, NEW_MAX, My_Sum+TOTAL_SUM,TOTAL_NUMBERS+My_length,1,1,1);
		{pull_request , Pid} ->
			if 
				AVERAGE == 0 -> 
					NEW_TOTAL_SUM_1 = lists:sum(Data_Values),
					NEW_TOTAL_NUM_1 = length(Data_Values);
				true ->
					NEW_TOTAL_SUM_1 = AVERAGE,
					NEW_TOTAL_NUM_1 = MEDIAN
			end,
			Pid ! {push_average_request,self(),Data_Values,NEW_TOTAL_SUM_1,NEW_TOTAL_NUM_1,MIN,MAX},
			myGossip(Fragment_Id, Data_Values , Neighbours_List, Delay , KCount , MIN,MAX,AVERAGE,MEDIAN,1,1,1)
	end.




%%Please note Limitation that N always HAS to be EVEN!!! I will fix this later if needed, but as PoC this is just fine.
getAvg(N, K) ->
	
	%% K is derived from the demers, epidemic paper the K-factor, for just log N rounds , 
	KCount = ceiling(math:log(N) * K),
	
	%% generate seemingly random data, but each node will have value list of [SeedValue + i , SeedValue + i + 1 ] where i = 1 to N
	%% the median , max , min , avg can be mathematically calculated here 
	SeedValue = 1.5,
	Values = lists:map(fun(T) -> [SeedValue + T , SeedValue + T +1] end, lists:seq(1, N)), % foreach 1,2,3,4...N
	io:format("BOOTSTRAP: List of values: ~p~n~n~n", [Values]),
	%%lists:foreach(fun(T) -> io:format("index ~p value ~p~n",[T,lists:nth(T,Values)]) end , lists:seq(1,N)).  %% io:format("~p~n~n~n",[Values]). %%This will print value of each line
	
	%%spawn N /2 processes on each VM. To spawn on more VM's just add another line of Pid's and then n has to be divisible by 3!!!
	Pids1 = lists:map(fun(T) -> spawn('pong@192.168.10.102',mygossip, myGossip, [0,[],[],0,KCount,0,0,0,0,0,0,0]) end, lists:seq(1, N div 2)),
	Pids = Pids1 ++ lists:map(fun(T) -> spawn('ping@192.168.10.101',mygossip, myGossip, [0,[],[],0,KCount,0,0,0,0,0,0,0]) end, lists:seq(1, N div 2)),
	%%%%(Fragment_Id, Data_Values , Neighbours_List, Delay , KCount , MIN,MAX,AVERAGE,MEDIAN,Initialized,Infected,IsRunning)%%%%
	io:format("BOOTSTRAP: List of pids: ~p~n~n~n", [Pids]),
	
	timer:sleep(1000),
	
	%% Combine Pid's with the list of values they will have, or Not...doesnt matter
	ProcessData = lists:zip(Pids, Values),
	io:format("BOOTSTRAP: List of pids with respective Data: ~p~n~n~n", [ProcessData]),
	

	%% TODO GENERATE NEIGHBOUR LIST FOR EACH PID, and INCLUDE ITSELF
	%% TODO : send state to EACH process as Data,  tuple = {action=initialize, fragment_index= i , fragment_data = Values[i] , neighbour_list = [] or Pids itself for a complete graph, delay = 0,KCount }
	%lists:foreach( fun(X) -> io:format("action=~p , pid=~p , fragment_id=~p , data= ~p , neighbour_list=~p~n", [initialize,lists:nth(X,Pids) ,X, lists:nth(X,Values),Pids])  end , lists:seq(1, N) ),
	lists:foreach( fun(X) -> lists:nth(X,Pids) ! {initialize,X,lists:nth(X,Values),Pids,0,KCount,-1,-1,0,0} end , lists:seq(1, N) ),
	%% the last tuple in the message is the initial values for min , max , average, median 
	
	timer:sleep(5000),

	hd(Pids) ! {initialize_average}.