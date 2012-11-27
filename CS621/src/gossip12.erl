%% Author: Milind
%% Created: Nov 22, 2012
%% Description: TODO: Add description to useless
-module(gossip12).
-compile([debug_info, export_all]).


%% To Generate the Topology of the graph
generateTopology(Pids, Flag) ->
	case(Flag) of
		1 -> 
			io:format("~p Pids In this Ring Topology~n",[Pids]),
			N = length(Pids),
			TempPids = Pids ++ Pids,
			Topology = lists:map(fun(T) -> [lists:nth(N+(T-1),TempPids),lists:nth(N+1+(T-1),TempPids)] end, lists:seq(1,N)),
			io:format("Ring Topology~n~p",[Topology]);
		2 ->
			io:format("~p Pids In this Expander Graph Topology connected to 3 nodes(Degree=3)~n",[Pids]),
			N = length(Pids),
			TempPids = Pids ++ Pids ++ Pids,
			Topology = lists:map(fun(T) -> [lists:nth(N+(T-1),TempPids),lists:nth(N+1+(T-1),TempPids),lists:nth(N+2+(T-1),TempPids)] end, lists:seq(1,N)),
			io:format("Expander Graph with Degree = 3~n~p",[Topology]);
		3 ->
			io:format("~p Pids In this Expander Graph Topology connected to 4 nodes(Degree = 4)~n",[Pids]),
			N = length(Pids),
			TempPids = Pids ++ Pids ++ Pids ++ Pids,
			Topology = lists:map(fun(T) -> [lists:nth(N+(T-1),TempPids),lists:nth(N+1+(T-1),TempPids),lists:nth(N+2+(T-1),TempPids),lists:nth(N+3+(T-1),TempPids)] end, lists:seq(1,N)),
			io:format("Expander Graph with Degree = 4~n~p",[Topology]);
		_ ->
			N = length(Pids),
			Topology = lists:map( fun(T) -> Pids end, lists:seq(1,N))
	end,
	Topology.

getCurrentTS() ->
	element(2,now()).

%% Note that this function will only use the 2nd arguments values if same are found
mergeDicts(Dict1,Dict2) ->
	dict:merge(fun(_,V1,V2) -> V2 end,Dict1,Dict2).

%% This function will accumulate values of all keys in a Dict into a single list. Valid when values are []
dictToValueList(Dict) ->
	dict:fold(fun(Key,Val,Acc) -> Acc++Val end, [],Dict).

%% Find median from a list
median(List) when is_list(List) ->
    SList = lists:sort(List),
    Length = length(SList),
    case even_or_odd(Length) of
        even -> [A,B] = lists:sublist(SList, round(Length/2), 2), (A+B)/2;
        odd  -> lists:nth( round((Length+1)/2), SList )
    end.

%% Find mean from a list
mean(List) when is_list(List) ->
	if
		length(List) == 0 ->
			MEAN = 0;
		true -> 
    		MEAN = lists:sum(List) / length(List)
	end,
	MEAN.
	

%% check if a number is even or odd
even_or_odd(Num) when is_integer(Num) ->
    if
        Num band 1 == 0 -> even;
        true            -> odd
    end.

getMinimum(MIN1,MIN2) ->
	if
				MIN1 < MIN2 ->
					NEW_MIN = MIN1;
				MIN2 < MIN1 ->
					NEW_MIN = MIN2;
				MIN1 == MIN2 ->
					NEW_MIN = MIN1
			end,
	NEW_MIN.

getMaximum(MAX1,MAX2) ->
	if
				MAX1 > MAX2 ->
					NEW_MAX = MAX1;
				MAX2 > MAX1 ->
					NEW_MAX = MAX2;
				MAX1 == MAX2 ->
					NEW_MAX = MAX1
			end,
	NEW_MAX.

getRandomNumber() ->
	{A1,A2,A3} = now(), 
    random:seed(A1, A2, A3),
	random:uniform(9999).

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

sendPull(Neighbours_list,Fragment_Id) ->
	{A1,A2,A3} = now(), 
    random:seed(A1, A2, A3),
	if 
		length(Neighbours_list) > 0 ->
			NewNodePID2 = lists:nth(random:uniform(length(Neighbours_list)), Neighbours_list) ,
				if 
							self() == NewNodePID2 ->
								%io:format("~p PULL SELF, just sleep ~n",[self()]);
								a;
							true ->
								PULL_ID=getRandomNumber()+Fragment_Id,
								%io:format("~p PULL TO ~p PULL_ID ~p~n",[self(),NewNodePID2,PULL_ID]),
								NewNodePID2 ! {pull_request,self(),PULL_ID}
				end;
		true ->
			a
	end,
	getCurrentTS().

sendPushMessage(Fragment_Id, Data_Values , Neighbours_List, Delay , KCount , MIN,MAX,SumSeen,NumbersSeen,Initialized,Infected,IsRunning,SentTimeStamp,NewNodePID2,MessageID,IS_PUSH) ->
	if 
		SumSeen =< 0 -> 
			NEW_TOTAL_SUM = lists:sum(Data_Values),
			NEW_TOTAL_NUM = length(Data_Values);
		SumSeen > 0 ->
			NEW_TOTAL_SUM = SumSeen,
			NEW_TOTAL_NUM = NumbersSeen
	end,% end of Average if
							   				
	%%  do a PUSH-PULL here. In the end this acts as both PUSH + PULL
	NewNodePID2 ! {push_request,self(),Data_Values,NEW_TOTAL_SUM,NEW_TOTAL_NUM, MIN, MAX,MessageID},
	%io:format("~p ~p TO ~p KCount ~p ~p_ID ~p~n",[self(),IS_PUSH,NewNodePID2,KCount,IS_PUSH,MessageID]),
	NOW_TS=getCurrentTS(),
	{NOW_TS,NEW_TOTAL_SUM,NEW_TOTAL_NUM}. %I return the current timestamp always
	
							
								
	

%%TODO problem here is that I will push a message and then goto receive always. Ideally i want to PUSH
myGossip(Fragment_Id, Data_Values , Neighbours_List, Delay , KCount , MIN,MAX,SumSeen,NumbersSeen,Initialized,Infected,IsRunning,SentTimeStamp) -> 
	{A1,A2,A3} = now(), 
	random:seed(A1, A2, A3),
	NEW_TIMESTAMP = getCurrentTS(),
	if
		KCount >0 ->
	if
	NEW_TIMESTAMP - SentTimeStamp  > 2 ->
	%%%io:format("~p Invoked ~n",[self()]),
	if 
		IsRunning == 1 , Infected == 1 ->
			%%%io:format("~p Running ~n",[self()]),
			if 
				Initialized == 0 ->
				   io:format("~p Not initialized yet ~n",[self()]);
				Initialized == 1 ->
					%%%io:format("~p Data has been initialized ~n",[self()]),
					%% Find a random neighbour from neighbour list
					timer:sleep(Delay), %% Sleep before a pull and a push
				if 
					length(Neighbours_List) > 0 ->
						NewNodePID2 = lists:nth(random:uniform(length(Neighbours_List)), Neighbours_List) ,
						if 
							self() == NewNodePID2 ->
								%io:format("~p SELF, just sleep,KCount ~p ~n",[self(),KCount]),
								Now_TS_1 = getCurrentTS(),
								myGossip(Fragment_Id, Data_Values , Neighbours_List, Delay , KCount , MIN,MAX,SumSeen,NumbersSeen,Initialized,Infected,IsRunning,Now_TS_1); %%add this TS to sent?
							true ->
											PUSH_ID=getRandomNumber()+Fragment_Id,
							   				io:format("~p PUSH TO ~p KCount ~p PUSH_ID ~p~n",[self(),NewNodePID2,KCount,PUSH_ID]),											
											{Now_TS,NEW_TOTAL_SUM,NEW_TOTAL_NUM} = sendPushMessage(Fragment_Id, Data_Values , Neighbours_List, Delay , KCount , MIN,MAX,SumSeen,NumbersSeen,Initialized,Infected,IsRunning,SentTimeStamp,NewNodePID2,PUSH_ID,"PUSH"),
											myGossip(Fragment_Id, Data_Values , Neighbours_List, Delay , KCount-1 , MIN,MAX,NEW_TOTAL_SUM,NEW_TOTAL_NUM,Initialized,Infected,IsRunning,Now_TS)		
						end; % end of self check if
					true ->
						io:format("~p NO NEIGHBOURS TO PING",[self()])
				end  % end of if neighbours are there to PING
			end; % end of initialized if

		IsRunning == 1 , Infected == 0 ->
			%%io:format("~p PULL Phase ONLY, not infected ~n",[self()]),
			sendPull(Neighbours_List,Fragment_Id),
			myGossip(Fragment_Id, Data_Values , Neighbours_List, Delay , KCount , MIN,MAX,SumSeen,NumbersSeen,Initialized,Infected,IsRunning,getCurrentTS());
	 	true ->
			io:format("~p Not Running yet.~n",[self()])
	end; %%end of IsRunning condition
	true ->
		%io:format("~p Not enough time NEWTS = ~p SentTimeStamp = ~p~n",[self(),NEW_TIMESTAMP,SentTimeStamp]),
		a
	end;
	true ->
		s
%		if
%			NumbersSeen > 0 ->
%				io:format("~p FINAL AVERAGE ~p MIN ~p MAX ~p KCount ~p ~n",[self(),SumSeen/NumbersSeen , MIN ,MAX,KCount]);
%			true ->
%				io:format("~p FINAL AVERAGE ~p MIN ~p MAX ~p KCount ~p ~n",[self(),lists:sum(Data_Values)/length(Data_Values), MIN ,MAX,KCount])
%		end
	end, %% end of KCount check
	%% if not initialized then wait for the initialize message
	%% now that PUSH or PULL message has been sent , wait for a response and then updated your state.
	%% NOTE: PULL's will have a response of no_infection if it contacts an uninfected node, and gets a proper message if contacts infected. After that, a PULL message will never be sent by the node.
	%% NOTE: PUSH will start ONLY after a node has been infected.
	receive

		{initialize, Fragment_Id_in, Data_Values_in , Neighbours_List_in, Delay_in , KCount_in , _,_,_,_} ->
			IN_MIN = getMinimum(lists:min(Data_Values_in),MIN),
			IN_MAX = getMaximum(lists:max(Data_Values_in),MAX),
			io:format("~p received initialize [id,values,KCount, MIN, MAX , neighbour] ~p:~p:~p:~p:~p ~n", [self(), Fragment_Id_in, Data_Values_in,KCount_in,IN_MIN,IN_MAX]),
			timer:sleep(5000),
			myGossip(Fragment_Id_in, Data_Values_in , Neighbours_List_in, Delay_in , KCount_in , IN_MIN ,IN_MAX,SumSeen,NumbersSeen,1,0,1,0);

		{initialize} ->
			io:format("~p received initialized message.~n",[self()]),
			timer:sleep(10000), % sleep 10 seconds in hope that ALL nodes have received their inits
			myGossip(Fragment_Id, Data_Values , Neighbours_List, Delay , KCount ,MIN,MAX,SumSeen,NumbersSeen,1,1,1,0);
		
		{pull_request,Pid,PULL_ID} ->
			%%io:format("~p PULL from ~p PULL_ID ~p~n",[self(),Pid,PULL_ID]),
			%% TODO send a PUSH message for current computation.
			if
				Infected == 1 ->
					{NowTS,TOTAL_SUM,TOTAL_NUM} = sendPushMessage(Fragment_Id, Data_Values , Neighbours_List, Delay , KCount , MIN,MAX,SumSeen,NumbersSeen,Initialized,Infected,IsRunning,SentTimeStamp,Pid,PULL_ID,"PULL"),
					myGossip(Fragment_Id, Data_Values , Neighbours_List, Delay , KCount , MIN,MAX,TOTAL_SUM,TOTAL_NUM,Initialized,Infected,IsRunning,SentTimeStamp);
				true ->
					myGossip(Fragment_Id, Data_Values , Neighbours_List, Delay , KCount , MIN,MAX,SumSeen,NumbersSeen,Initialized,Infected,IsRunning,SentTimeStamp)
			end;
		
		{push_request,Pid,Source_Data,TOTAL_SUM,TOTAL_NUMBERS, MIN_MESSAGE , MAX_MESSAGE,PUSH_ID_IN} ->					

			NEW_MIN = getMinimum(MIN_MESSAGE,MIN), % if unintialized , calculate from data list
			NEW_MAX = getMaximum(MAX_MESSAGE,MAX),
			if 
				SumSeen =< 0 ->
					My_Sum = lists:sum(Data_Values),
					My_length = length(Data_Values);
				SumSeen > 0 ->
					My_Sum = SumSeen,
					My_length = NumbersSeen
			end,
			Pid ! { response_push_request,self(),My_Sum,My_length, NEW_MIN , NEW_MAX,PUSH_ID_IN},
			Computed_Average = (My_Sum+TOTAL_SUM)/(TOTAL_NUMBERS+My_length),
			io:format("~p REQUEST ~p , src_sum ~p my_sum ~p , new AVG ~p MIN=~p MAX=~p PUSH_ID ~p~n",[self(),Pid,TOTAL_SUM,My_Sum,Computed_Average,NEW_MIN,NEW_MAX,PUSH_ID_IN]),
			myGossip(Fragment_Id, Data_Values , Neighbours_List, Delay , KCount , NEW_MIN ,NEW_MAX ,My_Sum+TOTAL_SUM,TOTAL_NUMBERS+My_length,1,1,1,SentTimeStamp);
		
		{response_push_request, Pid ,TOTAL_SUM,TOTAL_NUMBERS , MIN_MESSAGE , MAX_MESSAGE,PUSH_ID_RESP} ->
			NEW_MIN = getMinimum(MIN_MESSAGE,MIN),
			NEW_MAX = getMaximum(MAX_MESSAGE,MAX),
			My_Sum = SumSeen,
			My_length = NumbersSeen,
			Computed_Average = (TOTAL_SUM + My_Sum )/(TOTAL_NUMBERS+ My_length),
			io:format("~p RESPONSE ~p , resp_sum ~p my_sum ~p AVG ~p MIN=~p MAX=~p PUSH_ID ~p~n",[self(),Pid,TOTAL_SUM,My_Sum,Computed_Average,NEW_MIN,NEW_MAX,PUSH_ID_RESP]),
			myGossip(Fragment_Id, Data_Values , Neighbours_List, Delay , KCount , NEW_MIN, NEW_MAX, TOTAL_SUM + My_Sum,TOTAL_NUMBERS+ My_length,1,1,1,SentTimeStamp);
		
		{exit} ->
		if
			NumbersSeen > 0 ->
				io:format("~p FINAL AVERAGE ~p MIN ~p MAX ~p KCount ~p ~n",[self(),SumSeen/NumbersSeen , MIN ,MAX,KCount]);
			true ->
				io:format("~p FINAL AVERAGE ~p MIN ~p MAX ~p KCount ~p ~n",[self(),lists:sum(Data_Values)/ length(Data_Values), MIN ,MAX,KCount])
		end,
		exit(no_reason);	
		
		_ ->
			io:format("~p FINAL STATE: MIN=~p MAX=~p AVERAGE=~p ~n",[self(),MIN,MAX,SumSeen/NumbersSeen])
	
		%% Milind - I am so smart arent I?
		after 5000 ->
			%%io:format("~p timeout for receive, send a Push/Pull again~n",[self()]),
			myGossip(Fragment_Id, Data_Values , Neighbours_List, Delay , KCount , MIN,MAX,SumSeen,NumbersSeen,Initialized,Infected,IsRunning,0)
	end.




%%Please note Limitation that N always HAS to be EVEN!!! I will fix this later if needed, but as PoC this is just fine.
getGossip(N, K, Delay , KillTime) ->
	
	{A1,A2,A3} = now(), 
	random:seed(A1, A2, A3),
	
	%% K is derived from the demers, epidemic paper the K-factor, for just log N rounds , 
	KCount = ceiling(math:log(N)) * K,
	
	%% generate seemingly random data, but each node will have value list of [SeedValue + i , SeedValue + i + 1 ] where i = 1 to N
	%% the median , max , min , avg can be mathematically calculated here 
	SeedValue = 0.5,
	%Values = lists:map(fun(T) -> [SeedValue + T , SeedValue + T +1] end, lists:seq(1, N)), % foreach 1,2,3,4...N
	%Values = lists:map(fun(_) -> lists:map( fun(_) -> random:uniform(1000)+0.5 end  , lists:seq(1,random:uniform(3))) end, lists:seq(1,N)),
	Values = lists:map(fun(_) -> lists:map( fun(_) -> random:uniform(1000)+0.5 end  , lists:seq(1,10)) end, lists:seq(1,N)),
	io:format("BOOTSTRAP: List of values: ~p~n~n~n", [Values]),
	%%lists:foreach(fun(T) -> io:format("index ~p value ~p~n",[T,lists:nth(T,Values)]) end , lists:seq(1,N)).  %% io:format("~p~n~n~n",[Values]). %%This will print value of each line
	
	%%spawn N /2 processes on each VM. To spawn on more VM's just add another line of Pid's and then n has to be divisible by 3!!!
	DataDict = dict:new(),
	MID = N div 2,
	Pids1 = lists:map(fun(T) -> spawn('pong@192.168.10.102',gossip12, myGossip, [T,lists:nth(T,Values),[],Delay,KCount,lists:min(lists:nth(T,Values)),lists:max(lists:nth(T,Values)),lists:sum(lists:nth(T,Values)),length(lists:nth(T,Values)),0,0,0,0]) end, lists:seq(1, MID)),
	Pids = Pids1 ++ lists:map(fun(T) -> spawn('ping@192.168.10.101',gossip12, myGossip, [MID + T,lists:nth(MID + T,Values),[],Delay,KCount,lists:min(lists:nth(MID+T,Values)),lists:max(lists:nth(T+MID,Values)),lists:sum(lists:nth(T+MID,Values)),length(lists:nth(T+MID,Values)),0,0,0,0]) end, lists:seq(1, MID)),
	%%%%(Fragment_Id, Data_Values , Neighbours_List, Delay , KCount , MIN,MAX,AVERAGE,MEDIAN,Initialized,Infected,IsRunning)%%%%
	io:format("BOOTSTRAP: List of pids: ~p~n~n~n", [Pids]), 
	
	timer:sleep(1000),
	
	Topology = generateTopology(Pids,5),
	%io:format("~n~n TOPOLOGY= ~p~n~n",[Topology]),
	
	%% TODO GENERATE NEIGHBOUR LIST FOR EACH PID, and INCLUDE ITSELF
	%% TODO : send state to EACH process as Data,  tuple = {action=initialize, fragment_index= i , fragment_data = Values[i] , neighbour_list = [] or Pids itself for a complete graph, delay = 0,KCount }
	%lists:foreach( fun(X) -> io:format("action=~p , pid=~p , fragment_id=~p , data= ~p , neighbour_list=~p~n", [initialize,lists:nth(X,Pids) ,X, lists:nth(X,Values),Pids])  end , lists:seq(1, N) ),
	lists:nth(1,Pids) ! {initialize,1,lists:nth(1,Values),lists:nth(1,Topology),Delay,KCount,lists:min(lists:nth(1,Values)),lists:max(lists:nth(1,Values)),0,0},
	lists:foreach( fun(X) -> lists:nth(X,Pids) ! {initialize,X,lists:nth(X,Values),lists:nth(X,Topology),Delay,KCount,lists:min(lists:nth(X,Values)),lists:max(lists:nth(X,Values)),0,0} end , lists:seq(2, N) ),
	%% the last tuple in the message is the initial values for min , max , average, median 
	%% add this in Topology , ++lists:map(fun(_) -> lists:nth(X,Pids) end,lists:seq(1,length(lists:nth(X,Topology))))
	
	hd(Pids) ! {initialize},
	
	timer:sleep(KillTime*1000),

	FlatList = lists:flatten(Values),
	io:format("~n~nMALDI: MIN=~p MAX=~p AVERAGE=~p MEDIAN=~p~n~n",[lists:min(FlatList),lists:max(FlatList),mean(FlatList),median(FlatList)]),
	lists:foreach( fun(X) -> lists:nth(X,Pids) ! {exit} end , lists:seq(1, N) ),
	timer:sleep(5000),
	io:format("~n~nMALDI: MIN=~p MAX=~p AVERAGE=~p MEDIAN=~p~n~n",[lists:min(FlatList),lists:max(FlatList),mean(FlatList),median(FlatList)]).

