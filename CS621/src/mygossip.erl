%% Author: Milind
%% Created: Nov 22, 2012
%% Description: TODO: Add description to useless
-module(mygossip).
-compile([debug_info, export_all]).

pingAVG(Delay) -> 
	receive
        	{pingAVG, Sender, SenderIndex, ReceiverIndex, Targets, Counter} ->

		io:format("~p has selected Process ~p ~n", [Sender, self()]),
		
           	timer:sleep(Delay),
	
		ReceiverValue = element(2,lists:nth(ReceiverIndex, Targets)),
		SenderValue = element(2,lists:nth(SenderIndex, Targets)),
		Avg = float((SenderValue + ReceiverValue) / 2),
	
		TdeletedSender = lists:delete({Sender,SenderValue}, Targets),
		Tmedsender = [{Sender,Avg}],
		TappendNewSender = lists:append(TdeletedSender, Tmedsender),
		
		TdeletedReceiver = lists:delete({self(),ReceiverValue}, TappendNewSender),
		Tmedreceiver = [{self(),Avg}],
		TappendNewReceiver = lists:append(TdeletedReceiver, Tmedreceiver),
		
		TSorted = lists:sort(TappendNewReceiver),

		NewSenderIndex = ReceiverIndex,		
		NewReceiverIndex = random:uniform(length(Targets)),

		Counter1 = Counter - 1,
		
		if Counter1 < 1  -> 
			io:format("Average is ~p...~n", [Avg]);
		true ->
			element(1,lists:nth(NewReceiverIndex, Targets)) ! {pingAVG, self(), NewSenderIndex, NewReceiverIndex, TSorted, Counter1},
			pingAVG(Delay)
		end
	  end.

myGossip({Fragment_Id, Data_Values , Neighbours_List, Delay , KCount , {MIN,MAX,AVERAGE,MEDIAN}},Initialized,Infected) -> 
	if 
		Initialized == 0 ->
		   io:format("Not initialized with Data yet for pid ~p~n",[self()]);
		Initialized == 1 ->
			if Infected == 0 ->
				   %% TODO do a PULL here
				   io:format("",[]);			   		
			   Infected == 1 ->
				   %% TODO do a PUSH here
				   io:format("",[])
			end,
		   io:format("Data has been initialized for the pid ~p~n",[self()])
	end,
	   
	%% if not initialized then wait for the initialize message
	%% now that PUSH or PULL message has been sent , wait for a response and then updated your state.
	%% NOTE: PULL's will have a response of no_infection if it contacts an uninfected node, and gets a proper message if contacts infected. After that, a PULL message will never be sent by the node.
	%% NOTE: PUSH will start ONLY after a node has been infected.
	receive
		{initialize, Fragment_Id_in, Data_Values_in , Neighbours_List_in, Delay_in , KCount_in , {MIN_in,MAX_in,AVERAGE_in,MEDIAN_in}} ->
			io:format("~p received initialize message with [id,values,neighbour] ~p:~p:~p ~n", [self(), Fragment_Id_in, Data_Values_in,Neighbours_List_in]),
			myGossip({Fragment_Id_in, Data_Values_in , Neighbours_List_in, Delay_in , KCount_in , {MIN_in,MAX_in,AVERAGE_in,MEDIAN_in}},1,0);
		{push_average_request,Pid,Source_Sum} ->
			My_Sum = lists:sum(Data_Values),
			Pid ! { response_push_average,self(), My_Sum}, %can return either my sum or computed average
			io:format("Got message from pid ~p , source_sum ~p my_sum ~p~n",[Pid,Source_Sum,My_Sum]),
			myGossip({Fragment_Id, Data_Values , Neighbours_List, Delay , KCount , {MIN,MAX,(My_Sum+Source_Sum)/2.0,MEDIAN}},1,1);
		{response_push_average, Pid , Response_Sum } ->
			My_Sum = lists:sum(Data_Values),
			io:format("Got response from pid ~p response, Response_sum ~p my_sum ~p ",[Pid,Response_Sum,My_Sum]),
			myGossip({Fragment_Id, Data_Values , Neighbours_List, Delay , KCount-1 , {MIN,MAX,(My_Sum+Response_Sum)/2.0,MEDIAN}},1,1),
			timer:sleep(Delay)
	end.




%%Please note Limitation that N always HAS to be EVEN!!! I will fix this later if needed, but as PoC this is just fine.
getAvg(N, K) ->
	
	%% K is derived from the demers, epidemic paper the K-factor, for just log N rounds , 
	KCount = math:log(N) * K,
	
	%% generate seemingly random data, but each node will have value list of [SeedValue + i , SeedValue + i + 1 ] where i = 1 to N
	%% the median , max , min , avg can be mathematically calculated here 
	SeedValue = 1.5,
	Values = lists:map(fun(T) -> [SeedValue + T , SeedValue + T +1] end, lists:seq(1, N)), % foreach 1,2,3,4...N
	io:format("List of values: ~p~n~n~n", [Values]),
	%%lists:foreach(fun(T) -> io:format("index ~p value ~p~n",[T,lists:nth(T,Values)]) end , lists:seq(1,N)).  %% io:format("~p~n~n~n",[Values]). %%This will print value of each line
	
	%%spawn N /2 processes on each VM. To spawn on more VM's just add another line of Pid's and then n has to be divisible by 3!!!
	Pids1 = lists:map(fun(T) -> spawn('pong@192.168.10.102',mygossip, myGossip, [0]) end, lists:seq(1, N div 2)),
	Pids = Pids1 ++ lists:map(fun(T) -> spawn('ping@192.168.10.101',mygossip, myGossip, [0]) end, lists:seq(1, N div 2)),
	io:format("List of pids: ~p~n~n~n", [Pids]),
	
	timer:sleep(1000),
	
	%% Combine Pid's with the list of values they will have, or Not...doesnt matter
	ProcessData = lists:zip(Pids, Values),
	io:format("List of pids: ~p~n~n~n", [ProcessData]),
	

	%% TODO GENERATE NEIGHBOUR LIST FOR EACH PID, and INCLUDE ITSELF
	%% TODO : send state to EACH process as Data,  tuple = {action=initialize, fragment_index= i , fragment_data = Values[i] , neighbour_list = [] or Pids itself for a complete graph, delay = 0,KCount }
	%lists:foreach( fun(X) -> io:format("action=~p , pid=~p , fragment_id=~p , data= ~p , neighbour_list=~p~n", [initialize,lists:nth(X,Pids) ,X, lists:nth(X,Values),Pids])  end , lists:seq(1, N) ),
	lists:foreach( fun(X) -> lists:nth(X,Pids) ! {initialize,X,lists:nth(X,Values),Pids,0,K,{0,0,0,0}} end , lists:seq(1, N) ),
	%% the last tuple in the message is the initial values for min , max , average, median 

	hd(Pids) ! {initialize_average}.