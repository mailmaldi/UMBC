%% Author: Milind (maldi) Patil.
%% Created: Nov 22, 2012
%% Description: TODO: Add description to useless
-module(gossip4).
-compile([debug_info, export_all]).

sendPushMessage(Fragment_Id, Data_Values,VERSION , Neighbours_List, Delay , KCount ,Initialized,Infected,IsRunning,SentTimeStamp,NewNodePID2,MessageID,IS_PUSH,UpdateTuple) ->
								   				
	NewNodePID2 ! {push_request,self(),MessageID,UpdateTuple}, 
	io:format("~p ~p ~p , KCount = ~p , ~p_ID ~p~n",[self(),IS_PUSH,NewNodePID2,KCount,IS_PUSH,MessageID]),
	
	receive
		%% TODO milind -> decide if you really want to send responses or with stop message- but for N replicas, maybe not, or just let update propagate
		{response_push_request, Pid ,PUSH_ID_RESP} ->
			io:format("~p RESPONSE ~p , PUSH_ID= ~p~n",[self(),Pid,PUSH_ID_RESP])
	
	after (1*Delay*1000+1000) ->
			%%TODO do something?
			a
	end,		

	NOW_TS=maldi:getCurrentTS(),
	{NOW_TS}. %I return the current timestamp always
	
	

%%TODO problem here is that I will push a message and then goto receive always. Ideally i want to PUSH
myGossip(Fragment_Id, Data_Values,VERSION , Neighbours_List, Delay , KCount ,Initialized,Infected,IsRunning,SentTimeStamp, UpdateTuple ) -> 
	
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
						myGossip(Fragment_Id, Data_Values,VERSION , Neighbours_List, Delay , KCount , Initialized,Infected,IsRunning,Now_TS_1,UpdateTuple); %%add this TS to sent?
					true ->
						PUSH_ID=maldi:getRandomNumber()+Fragment_Id,
						%io:format("~p PUSH TO ~p KCount ~p PUSH_ID ~p~n",[self(),NewNodePID2,KCount,PUSH_ID]),											
						{NOW_TS_Resp} = sendPushMessage(Fragment_Id, Data_Values,VERSION , Neighbours_List, Delay , KCount,Initialized,Infected,IsRunning,SentTimeStamp,NewNodePID2,PUSH_ID,push,UpdateTuple),
						myGossip(Fragment_Id, Data_Values,VERSION , Neighbours_List, Delay , KCount-1 ,Initialized,Infected,IsRunning,NOW_TS_Resp,UpdateTuple)		
			end; % end of self check if	
		KCount > 0 , NEW_TIMESTAMP - SentTimeStamp  > Delay , IsRunning == 1 , Initialized == 1	, Infected == 0 ->
			%%io:format("~p PULL Phase ONLY, not infected ~n",[self()]),
			{_ , PULL_TS} = maldi:sendPull(Neighbours_List,Fragment_Id),
			myGossip(Fragment_Id, Data_Values,VERSION , Neighbours_List, Delay , KCount ,Initialized,Infected,IsRunning,PULL_TS,UpdateTuple);
		true ->
			a
			%io:format("~p No condition Matched ~n",[self()])
	end,
	
	%% if not initialized then wait for the initialize message
	%% now that PUSH or PULL message has been sent , wait for a response and then updated your state.
	%% NOTE: PULL's will have a response of no_infection if it contacts an uninfected node, and gets a proper message if contacts infected. After that, a PULL message will never be sent by the node.
	%% NOTE: PUSH will start ONLY after a node has been infected.
	receive

		{initialize, Fragment_Id_in, Data_Values_in,VERSION_IN , Neighbours_List_in, Delay_in , KCount_in } ->
			io:format("~p received initialize [id,values,version,KCount] ~p:~p:~p:~p ~n", [self(), Fragment_Id, Data_Values,VERSION,KCount]),
			myGossip(Fragment_Id, Data_Values,VERSION , Neighbours_List_in, Delay, KCount , 1,0,1,0,UpdateTuple);

		{initialize, TASK,Fragment_Update_Id,New_Data_List,Version} ->
			io:format("~p,Initialize,Task=~p,To_Update=~p,New_Data=~p,New_Version=~p~n",[self(), TASK,Fragment_Update_Id,New_Data_List,Version]),
			UpdateTuple_New = {TASK,Fragment_Update_Id,New_Data_List,Version},
			timer:sleep(10000), % sleep 10 seconds in hope that ALL nodes have received their inits
			myGossip(Fragment_Id, Data_Values,VERSION , Neighbours_List, Delay , KCount,1,1,1,0,UpdateTuple_New);
		
		{pull_request,Pid,PULL_ID} ->
			%%io:format("~p PULL from ~p PULL_ID ~p~n",[self(),Pid,PULL_ID]),
			%% TODO send a PUSH message for current computation.
			if
				Infected == 1,Initialized == 1 ->
					{NOW_TS_N} = sendPushMessage(Fragment_Id, Data_Values,VERSION , Neighbours_List, Delay , KCount ,Initialized,Infected,IsRunning,SentTimeStamp,Pid,PULL_ID,pull,UpdateTuple),
					myGossip(Fragment_Id, Data_Values,VERSION , Neighbours_List, Delay , KCount ,Initialized,Infected,IsRunning,NOW_TS_N,UpdateTuple);
				true ->
					myGossip(Fragment_Id, Data_Values,VERSION , Neighbours_List, Delay , KCount ,Initialized,Infected,IsRunning,SentTimeStamp,UpdateTuple)
			end;
		
		{push_request,Pid,PUSH_ID_IN,UpdateTuple_In} ->					

			%%TODO if not initialized , then do something? Note that for these problems, initialize messages ONLY pass neighbour values, everything is there when spawned itself
			Pid ! { response_push_request,self(),PUSH_ID_IN},
			if
				tuple_size(UpdateTuple_In) == 0 ->
					io:format("~p,EMPTY_UPDATE_TUPLE~n",[self()]);
				true ->
					{TASK,FRAG_ID,NEW_VALUES,VERSION_IN}	= UpdateTuple_In,
					if
						FRAG_ID == Fragment_Id , VERSION_IN > VERSION , TASK == update ->
							io:format("~p,FragmentId=~p,Update_Version=~p,Current_Version=~p~n",[self(),Fragment_Id,VERSION_IN,VERSION]),
							NewDataList = NEW_VALUES,
							NewVersion= VERSION_IN;
						FRAG_ID == Fragment_Id , VERSION_IN =< VERSION ->
							NewDataList = Data_Values,
							NewVersion = VERSION;
						true ->
							NewDataList = Data_Values,
							NewVersion = VERSION
					end,
					io:format("~p REQUEST ~p , PUSH_ID= ~p~n",[self(),Pid,PUSH_ID_IN]),
					myGossip(Fragment_Id, NewDataList, NewVersion, Neighbours_List, Delay , KCount,1,1,1,SentTimeStamp,UpdateTuple_In)
			end;
			
		
		{response_push_request, Pid ,PUSH_ID_RESP} ->
			io:format("~p RESPONSE ~p , PUSH_ID= ~p~n",[self(),Pid,PUSH_ID_RESP]),
			myGossip(Fragment_Id, Data_Values,VERSION , Neighbours_List, Delay , KCount ,1,1,1,SentTimeStamp,UpdateTuple);
		
		{exit} ->
			io:format("~p,FINALSTATE,FragId=~p,KCount=~p,Data_Values=~p,VERSION=~p~n",[self(),Fragment_Id,KCount,Data_Values,VERSION]),
			exit(no_reason)	
		
		%% Milind - I am so smart arent I?
		after (Delay*1000+1000) ->
			%%io:format("~p timeout for receive, send a Push/Pull again~n",[self()]),
			myGossip(Fragment_Id, Data_Values,VERSION , Neighbours_List, Delay , KCount,Initialized,Infected,IsRunning,0,UpdateTuple)
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
	Values = lists:map(fun(_) -> lists:map( fun(_) ->random:uniform(10000)+0.5 end  , lists:seq(1,3)) end, lists:seq(1,N)),
	%io:format("BOOTSTRAP: List of values: ~p~n~n~n", [Values]),
	%%lists:foreach(fun(T) -> io:format("index ~p value ~p~n",[T,lists:nth(T,Values)]) end , lists:seq(1,N)).  %% io:format("~p~n~n~n",[Values]). %%This will print value of each line
	
	%%spawn N /2 processes on each VM. To spawn on more VM's just add another line of Pid's and then n has to be divisible by 3!!!
	MID = N div 2,
	Pids1 = lists:map(fun(T) ->
						Nth = lists:nth(T,Values),
						spawn('pong@192.168.10.102',?MODULE, myGossip, [T,Nth,1,[],Delay,KCount,0,0,0,0,{}]) end, lists:seq(1, MID)
					  ),

	Pids2 = lists:map(fun(T) -> 
						Nth = lists:nth(T,Values),
						spawn('ping@192.168.10.101',?MODULE, myGossip, [T,Nth,1,[],Delay,KCount,0,0,0,0,{}]) end, lists:seq(1, MID)
					  ),

	Pids = Pids1 ++ Pids2,

	%io:format("BOOTSTRAP: List of pids: ~p~n~n~n", [Pids]),
	io:format("BOOTSTRAP: Size of pids: ~p~n~n~n", [length(Pids)]), 
	
	timer:sleep(1000),
	
	Topology = maldi:generateTopology(Pids,TopologyId),
	%io:format("~n~n TOPOLOGY= ~p~n~n",[Topology]),
	io:format("~n~n ~p entries in TOPOLOGY Size = ~p~n~n",[length(Topology), length(lists:nth(1,Topology))]),
	
	%% lists:nth(1,Pids) ! {initialize,1,lists:nth(1,Values),1,lists:nth(1,Topology),Delay,KCount},
	lists:foreach( fun(X) -> lists:nth(X,Pids) ! {initialize,X,lists:nth(X,Values),1,lists:nth(X,Topology),Delay,KCount} end , lists:seq(1, N)),
	
	timer:sleep(10000),
	
	hd(Pids) ! {initialize, update,MID-1,[3.5,4.5,5.5,6.5],2},
	
	timer:sleep(KillTime*1000),

	lists:foreach( fun(X) -> lists:nth(X,Pids) ! {exit} end , lists:seq(1, N) ).
