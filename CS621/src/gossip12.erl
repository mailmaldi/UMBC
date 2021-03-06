%% Author: Milind (maldi) Patil.
%% Created: Nov 22, 2012
%% Description: TODO: Add description to useless
-module(gossip12).
-compile([debug_info, export_all]).

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
	NewNodePID2 ! {push_request,self(),NEW_TOTAL_SUM,NEW_TOTAL_NUM, MIN, MAX,MessageID,IS_PUSH}, %%TODO remove Data_Values for min,max,avg
	%io:format("~p ~p ~p,~p_ID=~p,KCount=~p~n",[self(),IS_PUSH,NewNodePID2,IS_PUSH,MessageID,KCount]),
	
	receive
		
		{response_push_request, Pid ,TOTAL_SUM,TOTAL_NUMBERS , MIN_MESSAGE , MAX_MESSAGE,PUSH_ID_RESP,IS_PUSH_In} ->
			NEW_MIN = maldi:getMinimum(MIN_MESSAGE,MIN),
			NEW_MAX = maldi:getMaximum(MAX_MESSAGE,MAX),
			My_Sum = SumSeen,
			My_length = NumbersSeen,
			Response_Sum = TOTAL_SUM + My_Sum,
			Response_Num = TOTAL_NUMBERS+ My_length,
			Computed_Average = (Response_Sum )/(Response_Num),
			io:format("~p ~p_RESPONSE ~p ,KCount=~p,AVGB=~p,AVGF=~p,MINB=~p,MINF=~p,MAXB=~p,MAXF=~p,~p_ID=~p~n",[Pid,IS_PUSH_In,self(),KCount,NEW_TOTAL_SUM/NEW_TOTAL_NUM,Computed_Average,MIN,NEW_MIN,MAX,NEW_MAX,IS_PUSH_In,PUSH_ID_RESP])
			%io:format("~p RESPONSE ~p , AVG= ~p , MIN=~p , MAX=~p , PUSH_ID= ~p~n",[self(),Pid,Computed_Average,NEW_MIN,NEW_MAX,PUSH_ID_RESP])
	
	after (3*Delay*1000+1000) ->
		NEW_MIN = MIN,
		NEW_MAX = MAX,
		Response_Sum = SumSeen,
		Response_Num = NumbersSeen		
	end,		

	NOW_TS=maldi:getCurrentTS(),
	{NOW_TS,NEW_MIN,NEW_MAX,Response_Sum,Response_Num}. %I return the current timestamp always
	
	

%%TODO problem here is that I will push a message and then goto receive always. Ideally i want to PUSH
myGossip(Fragment_Id, Data_Values , Neighbours_List, Delay , KCount , MIN,MAX,SumSeen,NumbersSeen,Initialized,Infected,IsRunning,SentTimeStamp) -> 
	
	{A1,A2,A3} = now(), 
	random:seed(A1, A2, A3),
	NEW_TIMESTAMP = maldi:getCurrentTS(),
	
	if
		KCount > 0 , NEW_TIMESTAMP - SentTimeStamp  > Delay , IsRunning == 1 ,Initialized == 0 ->
			%% If process hasnt been initialized with data, whats the point of doing any gossip?
			io:format("~p Not initialized yet ~n",[self()]);
		KCount > 0 , NEW_TIMESTAMP - SentTimeStamp  > Delay , IsRunning == 1 , Initialized == 1 , Infected == 1 ,  length(Neighbours_List) > 0 ->
			%% The PUSH phase...Only gets activated once infected. Initially only 1 node is infected,and it slowly spreads thru the n/w. We try to compute how many messages it takes for 1 to get infected
			NewNodePID2 = lists:nth(random:uniform(length(Neighbours_List)), Neighbours_List) ,
			if 
					self() == NewNodePID2 ->
						%io:format("~p SELF, just sleep,KCount ~p ~n",[self(),KCount]),
						Now_TS_1 = maldi:getCurrentTS(),
						myGossip(Fragment_Id, Data_Values , Neighbours_List, Delay , KCount , MIN,MAX,SumSeen,NumbersSeen,Initialized,Infected,IsRunning,Now_TS_1); %%add this TS to sent?
					true ->
						PUSH_ID=maldi:getRandomNumber()+Fragment_Id,
						%io:format("~p PUSH TO ~p KCount ~p PUSH_ID ~p~n",[self(),NewNodePID2,KCount,PUSH_ID]),											
						{NOW_TS_Resp,NEW_MIN_Resp,NEW_MAX_Resp,Response_Sum,Response_Num} = sendPushMessage(Fragment_Id, Data_Values , Neighbours_List, Delay , KCount , MIN,MAX,SumSeen,NumbersSeen,Initialized,Infected,IsRunning,SentTimeStamp,NewNodePID2,PUSH_ID,push),
						myGossip(Fragment_Id, Data_Values , Neighbours_List, Delay , KCount-1 , NEW_MIN_Resp,NEW_MAX_Resp,Response_Sum,Response_Num,Initialized,Infected,IsRunning,NOW_TS_Resp)		
			end; % end of self check if	
		KCount > 0 , NEW_TIMESTAMP - SentTimeStamp  > Delay , IsRunning == 1 , Initialized == 1	, Infected == 0 ->
			%%io:format("~p PULL Phase ONLY, not infected ~n",[self()]),
			{K , PULL_TS} = maldi:sendPull(Neighbours_List,Fragment_Id),
			myGossip(Fragment_Id, Data_Values , Neighbours_List, Delay , KCount , MIN,MAX,SumSeen,NumbersSeen,Initialized,Infected,IsRunning,PULL_TS);
		true ->
			a
			%io:format("~p No condition Matched ~n",[self()])
	end,
	
	%% if not initialized then wait for the initialize message
	%% now that PUSH or PULL message has been sent , wait for a response and then updated your state.
	%% NOTE: PULL's will have a response of no_infection if it contacts an uninfected node, and gets a proper message if contacts infected. After that, a PULL message will never be sent by the node.
	%% NOTE: PUSH will start ONLY after a node has been infected.
	receive

		{initialize, Fragment_Id_in, Data_Values_in , Neighbours_List_in, Delay_in , KCount_in , _,_,_,_} ->
			IN_MIN = maldi:getMinimum(lists:min(Data_Values_in),MIN),
			IN_MAX = maldi:getMaximum(lists:max(Data_Values_in),MAX),
			%io:format("~p received initialize [id,values,KCount, MIN, MAX , neighbour] ~p:~p:~p:~p:~p ~n", [self(), Fragment_Id_in, Data_Values_in,KCount_in,IN_MIN,IN_MAX]),
			myGossip(Fragment_Id_in, Data_Values_in , Neighbours_List_in, Delay_in , KCount_in , IN_MIN ,IN_MAX,SumSeen,NumbersSeen,1,0,1,0);

		{initialize} ->
			io:format("~n~n~n~n~p received initialized message.~n~n~n~n",[self()]),
			timer:sleep(10000), % sleep 10 seconds in hope that ALL nodes have received their inits
			myGossip(Fragment_Id, Data_Values , Neighbours_List, Delay , KCount ,MIN,MAX,SumSeen,NumbersSeen,1,1,1,0);
		
		{pull_request,Pid,PULL_ID} ->
			io:format("~p pull ~p,pull_ID=~p~n",[Pid,self(),PULL_ID]),
			if
				Infected == 1,Initialized == 1 ->
					{NOW_TS_N,NEW_MIN_N,NEW_MAX_N,Response_Sum_N,Response_Num_N} = sendPushMessage(Fragment_Id, Data_Values , Neighbours_List, Delay , KCount , MIN,MAX,SumSeen,NumbersSeen,Initialized,Infected,IsRunning,SentTimeStamp,Pid,PULL_ID,pull),
					myGossip(Fragment_Id, Data_Values , Neighbours_List, Delay , KCount , NEW_MIN_N,NEW_MAX_N,Response_Sum_N,Response_Num_N,Initialized,Infected,IsRunning,NOW_TS_N);
				true ->
					myGossip(Fragment_Id, Data_Values , Neighbours_List, Delay , KCount , MIN,MAX,SumSeen,NumbersSeen,Initialized,Infected,IsRunning,maldi:getCurrentTS())
			end;
		
		{push_request,Pid,TOTAL_SUM,TOTAL_NUMBERS, MIN_MESSAGE , MAX_MESSAGE,PUSH_ID_IN,IS_PUSH} ->					

			%%TODO if not initialized , then do something? Note that for these problems, initialize messages ONLY pass neighbour values, everything is there when spawned itself
			NEW_MIN = maldi:getMinimum(MIN_MESSAGE,MIN), % if unintialized , calculate from data list
			NEW_MAX = maldi:getMaximum(MAX_MESSAGE,MAX),
			if 
				SumSeen =< 0 ->
					My_Sum = lists:sum(Data_Values),
					My_length = length(Data_Values);
				SumSeen > 0 ->
					My_Sum = SumSeen,
					My_length = NumbersSeen
			end,
			Pid ! { response_push_request,self(),My_Sum,My_length, NEW_MIN , NEW_MAX,PUSH_ID_IN,IS_PUSH},
			Computed_Average = (My_Sum+TOTAL_SUM)/(TOTAL_NUMBERS+My_length),
			io:format("~p ~p_REQUEST ~p ,KCount=~p,AVGB=~p,AVGF=~p,MINB=~p,MINF=~p,MAXB=~p,MAXF=~p,~p_ID=~p~n",[Pid,IS_PUSH,self(),KCount,My_Sum/My_length,Computed_Average,MIN,NEW_MIN,MAX,NEW_MAX,IS_PUSH,PUSH_ID_IN]),
			myGossip(Fragment_Id, Data_Values , Neighbours_List, Delay , KCount , NEW_MIN ,NEW_MAX ,My_Sum+TOTAL_SUM,TOTAL_NUMBERS+My_length,1,1,1,maldi:getCurrentTS());
		
		{response_push_request, Pid ,TOTAL_SUM,TOTAL_NUMBERS , MIN_MESSAGE , MAX_MESSAGE,PUSH_ID_RESP,IS_PUSH_In} ->
			NEW_MIN = maldi:getMinimum(MIN_MESSAGE,MIN),
			NEW_MAX = maldi:getMaximum(MAX_MESSAGE,MAX),
			My_Sum = SumSeen,
			My_length = NumbersSeen,
			Computed_Average = (TOTAL_SUM + My_Sum )/(TOTAL_NUMBERS+ My_length),
			io:format("~p ~p_RESPONSE ~p ,KCount=~p,AVGB=~p,AVGF=~p,MINB=~p,MINF=~p,MAXB=~p,MAXF=~p,~p_ID=~p~n",[Pid,IS_PUSH_In,self(),KCount,My_Sum/My_length,Computed_Average,MIN,NEW_MIN,MAX,NEW_MAX,IS_PUSH_In,PUSH_ID_RESP]),
			myGossip(Fragment_Id, Data_Values , Neighbours_List, Delay , KCount , NEW_MIN, NEW_MAX, TOTAL_SUM + My_Sum,TOTAL_NUMBERS+ My_length,1,1,1,maldi:getCurrentTS());
		
		{exit} ->
			if
				NumbersSeen > 0 ->
					io:format("~p AVGF= ~p, MINF= ~p, MAXF= ~p,  MINI=~p, MAXI=~p, AVGI=~p, MEDIANI=~p, KCount= ~p ~n",[self(),SumSeen/NumbersSeen , MIN ,MAX,lists:min(Data_Values),lists:max(Data_Values),lists:sum(Data_Values)/length(Data_Values),maldi:median(Data_Values),KCount]);
				true ->
					io:format("~p AVGF= ~p, MINF= ~p, MAXF= ~p,  MINI=~p, MAXI=~p, AVGI=~p, MEDIANI=~p, KCount= ~p ~n",[self(),lists:sum(Data_Values)/ length(Data_Values), MIN ,MAX,lists:min(Data_Values),lists:max(Data_Values),lists:sum(Data_Values)/length(Data_Values),maldi:median(Data_Values),KCount])
			end,
			exit(no_reason)	
		
		%% Milind - I am so smart arent I?
		after (Delay*1000+1000) ->
			%%io:format("~p timeout for receive, send a Push/Pull again~n",[self()]),
			myGossip(Fragment_Id, Data_Values , Neighbours_List, Delay , KCount , MIN,MAX,SumSeen,NumbersSeen,Initialized,Infected,IsRunning,0)
	end.




%%Please note Limitation that N always HAS to be EVEN!!! I will fix this later if needed, but as PoC this is just fine.
getGossip(N, TopologyId , K, Delay  ,KillTime) ->
	
	io:format("Size= ~p , Topology= ~p , KCount= ~p , Delay= ~p s, KillTime= ~p s~n",[N, TopologyId , K, Delay  ,KillTime]),
	
	{A1,A2,A3} = now(), 
	random:seed(A1, A2, A3),
	
	%% K is derived from the demers, epidemic paper the K-factor, for just log N rounds , 
	KCount = maldi:getLog(N) * K,
	
	%% generate seemingly random data, but each node will have value list of [SeedValue + i , SeedValue + i + 1 ] where i = 1 to N
	%% the median , max , min , avg can be mathematically calculated here 
	SeedValue = 0.5,
	%Values = lists:map(fun(T) -> [SeedValue + T , SeedValue + T +1] end, lists:seq(1, N)), % foreach 1,2,3,4...N
	%Values = lists:map(fun(_) -> lists:map( fun(_) -> random:uniform(1000)+0.5 end  , lists:seq(1,random:uniform(3))) end, lists:seq(1,N)),
	Values = lists:map(fun(_) -> lists:map( fun(_) ->random:uniform(10000)+0.5 end  , lists:seq(1,300)) end, lists:seq(1,N)),
	%io:format("BOOTSTRAP: List of values: ~p~n~n~n", [Values]),
	%%lists:foreach(fun(T) -> io:format("index ~p value ~p~n",[T,lists:nth(T,Values)]) end , lists:seq(1,N)).  %% io:format("~p~n~n~n",[Values]). %%This will print value of each line
	
	%%spawn N /2 processes on each VM. To spawn on more VM's just add another line of Pid's and then n has to be divisible by 3!!!
	DataDict = dict:new(),
	MID = N div 2,
	Pids1 = lists:map(fun(T) ->
						Nth = lists:nth(T,Values),
						MIN = lists:min(lists:nth(T,Values)), 
						MAX = lists:max(lists:nth(T,Values)),
						SUM = lists:sum(lists:nth(T,Values)),
						COUNT = length(lists:nth(T,Values)),
						spawn('pong@192.168.10.102',?MODULE, myGossip, [T,Nth,[],Delay,KCount,MIN,MAX,SUM,COUNT,0,0,0,0]) end, lists:seq(1, MID)
					  ),

	Pids2 = lists:map(fun(T) -> 
						Index = MID + T,
						Nth = lists:nth(Index,Values),
						MIN = lists:min(lists:nth(Index,Values)), 
						MAX = lists:max(lists:nth(Index,Values)),
						SUM = lists:sum(lists:nth(Index,Values)),
						COUNT = length(lists:nth(Index,Values)),
						spawn('ping@192.168.10.101',?MODULE, myGossip, [Index,Nth,[],Delay,KCount,MIN,MAX,SUM,COUNT,0,0,0,0]) end, lists:seq(1, MID)
					  ),

	Pids = Pids1 ++ Pids2,
	
	%%%%(Fragment_Id, Data_Values , Neighbours_List, Delay , KCount , MIN,MAX,AVERAGE,MEDIAN,Initialized,Infected,IsRunning)%%%%
	%io:format("BOOTSTRAP: List of pids: ~p~n~n~n", [Pids]),
	io:format("BOOTSTRAP: Size of pids: ~p~n~n~n", [length(Pids)]), 
	
	timer:sleep(1000),
	
	Topology = maldi:generateTopology(Pids,TopologyId),
	%io:format("~n~n TOPOLOGY= ~p~n~n",[Topology]),
	io:format("~n~n ~p entries in TOPOLOGY Size = ~p~n~n",[length(Topology), length(lists:nth(1,Topology))]),
	
	%% TODO GENERATE NEIGHBOUR LIST FOR EACH PID, and INCLUDE ITSELF
	%% TODO : send state to EACH process as Data,  tuple = {action=initialize, fragment_index= i , fragment_data = Values[i] , neighbour_list = [] or Pids itself for a complete graph, delay = 0,KCount }
	%lists:foreach( fun(X) -> io:format("action=~p , pid=~p , fragment_id=~p , data= ~p , neighbour_list=~p~n", [initialize,lists:nth(X,Pids) ,X, lists:nth(X,Values),Pids])  end , lists:seq(1, N) ),
	lists:nth(1,Pids) ! {initialize,1,lists:nth(1,Values),lists:nth(1,Topology),Delay,KCount,lists:min(lists:nth(1,Values)),lists:max(lists:nth(1,Values)),0,0},
	lists:foreach( fun(X) -> lists:nth(X,Pids) ! {initialize,X,lists:nth(X,Values),lists:nth(X,Topology),Delay,KCount,lists:min(lists:nth(X,Values)),lists:max(lists:nth(X,Values)),0,0} end , lists:seq(2, N) ),
	%% the last tuple in the message is the initial values for min , max , average, median 
	%% add this in Topology , ++lists:map(fun(_) -> lists:nth(X,Pids) end,lists:seq(1,length(lists:nth(X,Topology))))
	
	timer:sleep(5000),
	
	hd(Pids) ! {initialize},
	
	timer:sleep(KillTime*1000),

	FlatList = lists:flatten(Values),
	io:format("~n~nMALDI: MIN=~p MAX=~p AVERAGE=~p MEDIAN=~p~n~n",[lists:min(FlatList),lists:max(FlatList),maldi:mean(FlatList),maldi:median(FlatList)]),
	lists:foreach( fun(X) -> lists:nth(X,Pids) ! {exit} end , lists:seq(1, N) ),
	timer:sleep(5000),
	io:format("~n~nMALDI: MIN=~p MAX=~p AVERAGE=~p MEDIAN=~p~n~n",[lists:min(FlatList),lists:max(FlatList),maldi:mean(FlatList),maldi:median(FlatList)]).

