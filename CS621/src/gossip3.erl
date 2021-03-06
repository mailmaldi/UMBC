%% Author: Milind (maldi) Patil.
%% Created: Nov 22, 2012
%% Description: TODO: Add description to useless
-module(gossip3).
-compile([debug_info, export_all]).

sendPushMessage(Fragment_Id, Data_Values , Neighbours_List, Delay , KCount ,Initialized,Infected,IsRunning,SentTimeStamp,NewNodePID2,MessageID,IS_PUSH,DataDict) ->
					   				
	%%  do a PUSH-PULL here. In the end this acts as both PUSH + PULL
	NewNodePID2 ! {push_request,self(),MessageID,DataDict,IS_PUSH}, %%TODO remove Data_Values for min,max,avg
	%io:format("~p ~p ~p , KCount = ~p , ~p_ID ~p~n",[self(),IS_PUSH,NewNodePID2,KCount,IS_PUSH,MessageID]),
	
	receive
		
		{response_push_request, Pid ,PUSH_ID_RESP,DataDict_In,IS_PUSH_In} ->
			MergedDict = maldi:mergeDicts(DataDict,DataDict_In),
			Median_Old = maldi:median(maldi:dictToValueList(DataDict)),
			Median_New = maldi:median(maldi:dictToValueList(MergedDict)),
			io:format("~p ~p_RESPONSE ~p ,KCount=~p,MEDIANB=~p,MEDIANF=~p,~p_ID=~p~n",[Pid,IS_PUSH_In,self(),KCount,Median_Old,Median_New,IS_PUSH_In,PUSH_ID_RESP])
	
	after (3*Delay*1000+1000) ->
		MergedDict = DataDict
	end,		

	NOW_TS=maldi:getCurrentTS(),
	{NOW_TS,MergedDict}. %I return the current timestamp always
	
	

%% problem here is that I will push a message and then goto receive always. Ideally i want to PUSH
myGossip(Fragment_Id, Data_Values , Neighbours_List, Delay , KCount ,Initialized,Infected,IsRunning,SentTimeStamp,DataDict) -> 
	
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
						myGossip(Fragment_Id, Data_Values , Neighbours_List, Delay , KCount ,Initialized,Infected,IsRunning,Now_TS_1,DataDict); %%add this TS to sent?
					true ->
						PUSH_ID=maldi:getRandomNumber()+Fragment_Id,
						%io:format("~p PUSH TO ~p KCount ~p PUSH_ID ~p~n",[self(),NewNodePID2,KCount,PUSH_ID]),											
						{NOW_TS_Resp,DataDict_In} = sendPushMessage(Fragment_Id, Data_Values , Neighbours_List, Delay , KCount ,Initialized,Infected,IsRunning,SentTimeStamp,NewNodePID2,PUSH_ID,push,DataDict),
						myGossip(Fragment_Id, Data_Values , Neighbours_List, Delay , KCount-1 ,Initialized,Infected,IsRunning,NOW_TS_Resp,DataDict_In)		
			end; % end of self check if	
		KCount > 0 , NEW_TIMESTAMP - SentTimeStamp  > Delay , IsRunning == 1 , Initialized == 1	, Infected == 0 ->
			%%io:format("~p PULL Phase ONLY, not infected ~n",[self()]),
			{_ , PULL_TS} = maldi:sendPull(Neighbours_List,Fragment_Id),
			myGossip(Fragment_Id, Data_Values , Neighbours_List, Delay , KCount ,Initialized,Infected,IsRunning,PULL_TS,DataDict);
		true ->
			a
			%io:format("~p No condition Matched ~n",[self()])
	end,
	
	%% if not initialized then wait for the initialize message
	%% now that PUSH or PULL message has been sent , wait for a response and then updated your state.
	%% NOTE: PULL's will have a response of no_infection if it contacts an uninfected node, and gets a proper message if contacts infected. After that, a PULL message will never be sent by the node.
	%% NOTE: PUSH will start ONLY after a node has been infected.
	receive

		{initialize, Fragment_Id_in, Data_Values_in , Neighbours_List_in, Delay_in , KCount_in} ->
			myGossip(Fragment_Id_in, Data_Values_in , Neighbours_List_in, Delay_in , KCount_in ,1,0,1,0,DataDict);

		{initialize} ->
			io:format("~n~n~n~p received initialized message.~n~n~n",[self()]),
			timer:sleep(10000), % sleep 10 seconds in hope that ALL nodes have received their inits
			myGossip(Fragment_Id, Data_Values , Neighbours_List, Delay , KCount ,1,1,1,0,DataDict);
		
		{pull_request,Pid,PULL_ID} ->
			io:format("~p pull ~p,pull_ID=~p~n",[Pid,self(),PULL_ID]),
			if
				Infected == 1,Initialized == 1 ->
					{NOW_TS_N,DataDict_In2} = sendPushMessage(Fragment_Id, Data_Values , Neighbours_List, Delay , KCount , Initialized,Infected,IsRunning,SentTimeStamp,Pid,PULL_ID,pull,DataDict),
					myGossip(Fragment_Id, Data_Values , Neighbours_List, Delay , KCount,Initialized,Infected,IsRunning,NOW_TS_N,DataDict_In2);
				true ->
					myGossip(Fragment_Id, Data_Values , Neighbours_List, Delay , KCount,Initialized,Infected,IsRunning,SentTimeStamp,DataDict)
			end;
		
		{push_request,Pid,PUSH_ID_IN,DataDict_In2,IS_PUSH} ->					

			MergedDicts = maldi:mergeDicts(DataDict,DataDict_In2),
			NewDataDict = maldi:dictSubtract(DataDict,DataDict_In2), %% In response send back only values of difference
			Pid ! { response_push_request,self(),PUSH_ID_IN,NewDataDict,IS_PUSH},
			Median_Old = maldi:median(maldi:dictToValueList(DataDict)),
			Median_New = maldi:median(maldi:dictToValueList(MergedDicts)),
			io:format("~p ~p_REQUEST ~p ,KCount=~p,MEDIANB=~p,MEDIANF=~p,~p_ID=~p~n",[Pid,IS_PUSH,self(),KCount,Median_Old,Median_New ,IS_PUSH,PUSH_ID_IN]),
			myGossip(Fragment_Id, Data_Values , Neighbours_List, Delay , KCount ,1,1,1,SentTimeStamp,MergedDicts);
		
		{response_push_request, Pid ,PUSH_ID_RESP,DataDict_In2,IS_PUSH_In} ->
			MergedDicts = maldi:mergeDicts(DataDict,DataDict_In2),
			Median_Old = maldi:median(maldi:dictToValueList(DataDict)),
			Median_New = maldi:median(maldi:dictToValueList(MergedDicts)),
			io:format("~p ~p_RESPONSE ~p ,KCount=~p,MEDIANB=~p,MEDIANF=~p,~p_ID=~p~n",[Pid,IS_PUSH_In,self(),KCount,Median_Old,Median_New,IS_PUSH_In,PUSH_ID_RESP]),
			myGossip(Fragment_Id, Data_Values , Neighbours_List, Delay , KCount,1,1,1,SentTimeStamp,MergedDicts);
		
		{exit} ->
			FINAL_MEDIAN = maldi:median(maldi:dictToValueList(DataDict)),
			io:format("~pMEDIANB=~p,MEDIANF=~p,KCount=~p~n",[self(),maldi:median(Data_Values),FINAL_MEDIAN,KCount]),
			exit(no_reason)	
		
		%% Milind - I am so smart arent I?
		after (Delay*1000+1000) ->
			%%io:format("~p timeout for receive, send a Push/Pull again~n",[self()]),
			myGossip(Fragment_Id, Data_Values , Neighbours_List, Delay , KCount ,Initialized,Infected,IsRunning,0,DataDict)
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
	%SeedValue = 0.5,
	%Values = lists:map(fun(T) -> [SeedValue + T , SeedValue + T +1] end, lists:seq(1, N)), % foreach 1,2,3,4...N
	%Values = lists:map(fun(_) -> lists:map( fun(_) -> random:uniform(1000)+0.5 end  , lists:seq(1,random:uniform(3))) end, lists:seq(1,N)),
	Values = lists:map(fun(_) -> lists:map( fun(_) ->random:uniform(10000)+0.5 end  , lists:seq(1,2)) end, lists:seq(1,N)),
	FlatList = lists:flatten(Values),
	io:format("~n~nMALDI: MIN=~p MAX=~p AVERAGE=~p MEDIAN=~p~n~n",[lists:min(FlatList),lists:max(FlatList),maldi:mean(FlatList),maldi:median(FlatList)]),
	
	%io:format("BOOTSTRAP: List of values: ~p~n~n~n", [Values]),
	%%lists:foreach(fun(T) -> io:format("index ~p value ~p~n",[T,lists:nth(T,Values)]) end , lists:seq(1,N)).  %% io:format("~p~n~n~n",[Values]). %%This will print value of each line
	
	%%spawn N /2 processes on each VM. To spawn on more VM's just add another line of Pid's and then n has to be divisible by 3!!!
	DataDict = dict:new(),
	MID = N div 2,
	Pids1 = lists:map(fun(T) ->
						Nth = lists:nth(T,Values),
						Int_Dict = dict:store(T,Nth,DataDict),
						spawn('pong@192.168.10.102',?MODULE, myGossip, [T,Nth,[],Delay,KCount,0,0,0,0,Int_Dict]) end, lists:seq(1, MID)
					  ),

	Pids2 = lists:map(fun(T) -> 
						Index = MID + T,
						Nth = lists:nth(Index,Values),
						Int_Dict = dict:store(Index,Nth,DataDict),
						spawn('ping@192.168.10.101',?MODULE, myGossip, [Index,Nth,[],Delay,KCount,0,0,0,0,Int_Dict]) end, lists:seq(1, MID)
					  ),

	Pids = Pids1 ++ Pids2,

	%%%%(Fragment_Id, Data_Values , Neighbours_List, Delay , KCount , MIN,MAX,AVERAGE,MEDIAN,Initialized,Infected,IsRunning)%%%%
	%io:format("BOOTSTRAP: List of pids: ~p~n~n~n", [Pids]),
	io:format("BOOTSTRAP: Size of pids: ~p~n~n~n", [length(Pids)]), 
	
	timer:sleep(1000),
	
	Topology = maldi:generateTopology(Pids,TopologyId),
	%io:format("~n~n TOPOLOGY= ~p~n~n",[Topology]),
	io:format("~n~n ~p entries in TOPOLOGY Size = ~p~n~n",[length(Topology), length(lists:nth(1,Topology))]),
	
	%% GENERATE NEIGHBOUR LIST FOR EACH PID, and INCLUDE ITSELF
	lists:nth(1,Pids) ! {initialize,1,lists:nth(1,Values),lists:nth(1,Topology),Delay,KCount},
	lists:foreach( fun(X) -> lists:nth(X,Pids) ! {initialize,X,lists:nth(X,Values),lists:nth(X,Topology),Delay,KCount} end , lists:seq(2, N) ),
	
	timer:sleep(5000),
	
	hd(Pids) ! {initialize},
	
	timer:sleep(KillTime*1000),

	
	lists:foreach( fun(X) -> lists:nth(X,Pids) ! {exit} end , lists:seq(1, N) ),
	timer:sleep(5000),
	io:format("~n~nMALDI: MIN=~p MAX=~p AVERAGE=~p MEDIAN=~p~n~n",[lists:min(FlatList),lists:max(FlatList),maldi:mean(FlatList),maldi:median(FlatList)]).

