-module(gossip_fetch_i).
-compile([debug_info, export_all]).

gossip(MyPartition, N, MyFragments, MyFragmentIds, FragmentsImFetching, Mode, Delay, StatProcess) ->
	if(length(FragmentsImFetching)>0)->
		RoundsRemaining=lists:nth(3, FragmentsImFetching),
		FragmentFetchStatus = lists:nth(2, FragmentsImFetching),
		if (Mode == readyToSend), (RoundsRemaining > 0) ->
			%timer:sleep(100),
			RandomIndex = random:uniform(length(MyPartition)),
			RandomNode = lists:nth(RandomIndex, MyPartition),
			
			%if RandomNode =:= self() ->
				%io:format("~p gossipping with ~p~n",[self(), RandomNode]),
				%io:format("Message : ~p~n~n",[{self(), send, avg, MyFragments, MyPartition}]),
				%RandomNode ! {self(), fetch, true, FragmentsImFetching},
				%gossip(MyPartition, N, MyFragments, MyFragmentIds, FragmentsImFetching, awaitingReply, RoundsRemaining - 1, 0)
	
				case lists:nth(2, FragmentsImFetching) of
					iAmFetching-> %{Node_id, fetch, true, FragmentNodeIsFetching}
						StatProcess ! {fetch}, 
						RandomNode ! {self(), fetch, true, lists:nth(1, FragmentsImFetching)},
						gossip(MyPartition, N, MyFragments, MyFragmentIds, [lists:nth(1, FragmentsImFetching), iAmFetching, lists:nth(3, 	FragmentsImFetching)-1], awaitingReply, 0, StatProcess);

					fetch -> %{Node_id, fetch, true, FragmentNodeIsFetching}
						StatProcess ! {fetch},
						RandomNode !  {self(), fetch, true, lists:nth(1, FragmentsImFetching)},
						gossip(MyPartition, N, MyFragments, MyFragmentIds, [lists:nth(1, FragmentsImFetching), fetch, lists:nth(3, FragmentsImFetching)-1], readyToSend, 0, StatProcess);

					disseminate -> %{Node_id, fetch, dataIsHere, FragmentNodeIsFetching, Fragments}
						StatProcess ! {dataDisseminate},
						RandomNode ! {self(), fetch, dataIsHere, lists:nth(1, FragmentsImFetching), lists:nth(4, FragmentsImFetching)},
						gossip(MyPartition, N, MyFragments, MyFragmentIds, [lists:nth(1, FragmentsImFetching), disseminate, lists:nth(3, 	FragmentsImFetching)-1, lists:nth(4, FragmentsImFetching)], readyToSend, 0, StatProcess);
	
					false -> %{Node_id, fetch, false, FragmentNodeIsFetching}
						StatProcess ! {stopFetching},
						RandomNode ! {self(), fetch, false, lists:nth(1, FragmentsImFetching)},
						gossip(MyPartition, N, MyFragments, MyFragmentIds, [lists:nth(1, FragmentsImFetching), false, lists:nth(3, 	FragmentsImFetching)-1, lists:nth(4, FragmentsImFetching)], readyToSend, 0, StatProcess);
	
					myFragment -> %{Node_id, fetch, dataIsHere, FragmentNodeIsFetching, Fragments}
						StatProcess ! {dataDisseminate},
						RandomNode ! {self(), fetch, dataIsHere, MyFragmentIds, MyFragments},
						gossip(MyPartition, N, MyFragments, MyFragmentIds, [lists:nth(1, FragmentsImFetching), myFragment, lists:nth(3, 	FragmentsImFetching)-1], readyToSend, 0, StatProcess);
	
					fragmentReceived -> %{Node_id, fetch, false, FragmentNodeIsFetching}
						StatProcess ! {stopFetching},
						RandomNode ! {self(), fetch, false, lists:nth(1, FragmentsImFetching)},
						gossip(MyPartition, N, MyFragments, MyFragmentIds, [lists:nth(1, FragmentsImFetching), fragmentReceived, lists:nth(3, 	FragmentsImFetching)-1, lists:nth(4, FragmentsImFetching)], readyToSend, 0, StatProcess);
	
					timeout -> %%{Node_id, fetch, false, FragmentNodeIsFetching}
						StatProcess ! {stopFetching},			
						RandomNode ! {self(), fetch, false, lists:nth(1, FragmentsImFetching)},
						gossip(MyPartition, N, MyFragments, MyFragmentIds, [lists:nth(1, FragmentsImFetching), timeout, lists:nth(3, 	FragmentsImFetching)-1], readyToSend, 0, StatProcess)
				end;
			%true -> gossip(MyPartition, N, MyFragments, MyFragmentIds, [lists:nth(1, FragmentsImFetching), iAmFetching, lists:nth(3, 	FragmentsImFetching)], readyToSend, 0)
			%timer:sleep(200)
			%end;
			
		Mode == readyToSend, RoundsRemaining == 0 ->
			if 
				FragmentFetchStatus == fragmentReceived -> 
					io:format("Fragment Received EXITING"),
					exit("Fragment Received");
	
				true -> gossip(MyPartition, N, MyFragments, MyFragmentIds, FragmentsImFetching, receiveOnly, 0, StatProcess)
					%io:format("done gossiping....~p exiting~n", [self()])
			end;
		
		true -> a
		end;
	true-> a
	end,
	
	
	receive
		{Node, tellFragmentId} ->
			Node ! {MyFragmentIds, MyFragments};
			
		{fetchStart, FragmentId, SendToId} -> 
			if MyFragmentIds == FragmentId ->
				io:format("I (~p) own fragment id ~p~n....exiting~n",[self(), FragmentId]),
				exit("self owned fragment");
			true->
				RandomIndexInit = random:uniform(length(MyPartition)),
				RandomNodeInit = lists:nth(RandomIndexInit, MyPartition),
				%io:format("Starting fetch fragment id ~p at ~p ~n",[FragmentId, self()]),
				%io:format("Message : ~p~n~n",[{self(), fetch, true, FragmentId}]),
				SendToId ! {self(), fetch, true, FragmentId},
				StatProcess ! {fetch},
				gossip(MyPartition, N, MyFragments, MyFragmentIds, [FragmentId, iAmFetching, length(MyPartition)], awaitingReply, 0, StatProcess)
			end;
			
		{init, Pids} ->
			MappingFun = fun(A) ->
						X=self(),
						Y = lists:nth(A,Pids),
						if Y == X ->
							lists:nth(random:uniform(N), Pids);
						true -> lists:nth(A,Pids)
						end
					end,
			NewPartition = lists:map(fun(A) -> lists:nth(A, Pids) end, MyPartition),
			%NewPartition = lists:merge(lists:map(MappingFun, MyPartition), lists:map(fun(_)-> self() end, [self()])),
			%io:format("~p initialised :~n Partition: ~p~n FragmentId: ~p~n Values:~p~n~n", [self(), NewPartition, MyFragmentIds, MyFragments]),
			gossip(NewPartition, N, MyFragments, MyFragmentIds, [], initialised, 0, StatProcess);
			
			

		% please reply with fragment or fetch for it amongst your neighbors
		{Node_id, fetch, true, FragmentNodeIsFetching} ->
			%io:format("I am ~p....Received message : ~p~n", [self(),{Node_id, fetch, true, FragmentNodeIsFetching}]),

			% I don't have this fragment. Need to disseminate request for this fragment.
			if length(FragmentsImFetching)==0 ->
				
				if FragmentNodeIsFetching == MyFragmentIds ->
					%io:format("in fetch_true/0length/myData"),
					StatProcess ! {dataDisseminate},
					Node_id ! {self(), fetch, dataIsHere, FragmentNodeIsFetching, MyFragments},
					gossip(MyPartition, N, MyFragments, MyFragmentIds, [FragmentNodeIsFetching, myFragment, length(MyPartition)], readyToSend, 0, StatProcess);

				true ->
					%io:format("in fetch_true/0length/startFetch"),
					gossip(MyPartition, N, MyFragments, MyFragmentIds, [FragmentNodeIsFetching, fetch, length(MyPartition)], readyToSend, 0, StatProcess)
				end;
			true->
				%io:format("in fetch_true/true"),
				FragmentIdUnderway = lists:nth(1,FragmentsImFetching),
				FragmentStatusUnderway = lists:nth(2, FragmentsImFetching),
	
				case FragmentNodeIsFetching of
					% I have this fragment and I have to disseminate this fragment.				
					MyFragmentIds -> 
						StatProcess ! {dataDisseminate},
						Node_id ! {self(), fetch, dataIsHere, FragmentNodeIsFetching, 	MyFragments},						
						gossip(MyPartition, N, MyFragments, MyFragmentIds, FragmentsImFetching, readyToSend, 0, StatProcess);
					
					
					%it's the fragment i am fetching
					FragmentIdUnderway ->
						
						if 
							%the fragment has already been received by fetching process
							FragmentStatusUnderway == fragmentReceived; FragmentStatusUnderway == false -> 
								StatProcess ! {stopFetching},
								Node_id ! {self(), fetch, false, FragmentNodeIsFetching},
								gossip(MyPartition, N, MyFragments, MyFragmentIds, FragmentsImFetching, readyToSend, 0, StatProcess);
	
							% i am still waiting for the fragment to be fetched for me
							FragmentStatusUnderway == iAmFetching ->
								gossip(MyPartition, N, MyFragments, MyFragmentIds, FragmentsImFetching, readyToSend, 0, StatProcess);
	
							% i am disseminating the fragment to be fetched as it has been found
							FragmentStatusUnderway == disseminate ->
								StatProcess ! {dataDisseminate},
								Node_id ! {self(), fetch, dataIsHere, FragmentNodeIsFetching, lists:nth(4, FragmentsImFetching)},
								gossip(MyPartition, N, MyFragments, MyFragmentIds, FragmentsImFetching, readyToSend, 0, StatProcess);
							
							true ->
								gossip(MyPartition, N, MyFragments, MyFragmentIds, FragmentsImFetching, readyToSend, 0, StatProcess)
						end;
						
						
					
					_ -> 
						gossip(MyPartition, N, MyFragments, MyFragmentIds, FragmentsImFetching, readyToSend, 0, StatProcess)
				end		
			end;

		% stop fetching for this fragment and disseminate the same message
		{Node_id, fetch, false, FragmentNodeIsFetching} ->
			%io:format("I am ~p....Received message : ~p~n", [self(),{Node_id, fetch, false, FragmentNodeIsFetching}]),
			
			if 
				length(FragmentsImFetching)==0 ->
					gossip(MyPartition, N, MyFragments, MyFragmentIds, [FragmentNodeIsFetching, false, length(MyPartition)], readyToSend, 0, StatProcess);		
				true -> 
					FragmentStatusUnderway = lists:nth(2, FragmentsImFetching),
	
					if FragmentStatusUnderway =:= false ->			
						gossip(MyPartition, N, MyFragments, MyFragmentIds, [FragmentNodeIsFetching, false, length(MyPartition)], readyToSend, 0, StatProcess);

					true ->
						gossip(MyPartition, N, MyFragments, MyFragmentIds, FragmentsImFetching, readyToSend, 0, StatProcess)
					end
			end;
			

		% here is the data someone is fetching...please disseminate
		{Node_id, fetch, dataIsHere, FragmentNodeIsFetching, Fragments} ->
			%io:format("I am ~p....Received message : ~p~n", [self(),{Node_id, fetch, dataIsHere, FragmentNodeIsFetching, Fragments}]),
			if 
				length(FragmentsImFetching)==0 ->
					
					gossip(MyPartition, N, MyFragments, MyFragmentIds, [FragmentNodeIsFetching, disseminate, length(MyPartition), Fragments], readyToSend, 0, StatProcess);
				
				true ->
					FragmentIdUnderway = lists:nth(1,FragmentsImFetching),
					FragmentStatusUnderway = lists:nth(2, FragmentsImFetching),

					if (FragmentIdUnderway == FragmentNodeIsFetching), (FragmentStatusUnderway == iAmFetching) ->
						io:format("Fragment received : ~p",[Fragments]),
						StatProcess ! {dataRecieved},
						StatProcess ! {stopFetching},
						Node_id ! {self(), fetch, false, FragmentNodeIsFetching},
						gossip(MyPartition, N, MyFragments, MyFragmentIds, [FragmentNodeIsFetching, fragmentReceived, length(MyPartition), Fragments], readyToSend, 0, StatProcess);

					(FragmentIdUnderway == FragmentNodeIsFetching), (FragmentStatusUnderway == fragmentReceived) ->
						StatProcess ! {stopFetching},
						Node_id ! {self(), fetch, false, FragmentNodeIsFetching},
						gossip(MyPartition, N, MyFragments, MyFragmentIds, FragmentsImFetching, readyToSend, 0, StatProcess);

					% need to consider case of interim node which will forward the data as well as the one stopping mode
					(FragmentIdUnderway == FragmentNodeIsFetching), (FragmentStatusUnderway == fetch) ->
						gossip(MyPartition, N, MyFragments, MyFragmentIds, [FragmentNodeIsFetching, disseminate, length(MyPartition), Fragments], readyToSend, 0, StatProcess);
				
					(FragmentIdUnderway == FragmentNodeIsFetching), (FragmentStatusUnderway == false) ->
						StatProcess ! {stopFetching},
						Node_id ! {self(), fetch, false, FragmentNodeIsFetching},
						gossip(MyPartition, N, MyFragments, MyFragmentIds, FragmentsImFetching, readyToSend, 0, StatProcess);
	
					true -> gossip(MyPartition, N, MyFragments, MyFragmentIds, FragmentsImFetching, readyToSend, 0, StatProcess)

					end
			end;


		_ ->
			io:format("~p in Other shit~n", [self()])

	end	

.

average_final_values(FinalValues, Length, Index, Sum) ->
	if Index =< Length ->
		average_final_values(FinalValues, Length, Index+1, Sum+lists:nth(Index, FinalValues));
	true -> Sum/Length
	end
.

init_the_dhondus(N) ->
	StatProcess = spawn(gossip_fetch_i, statFun, [0,0, 0, N]),
	Values = lists:map(fun(_) -> random:uniform(100) end, lists:seq(0,N-1)),
	Fragments = lists:map(fun(_) -> lists:map(fun(_) -> random:uniform(100)+0.5 end, lists:seq(1, random:uniform(3))) end, lists:seq(0,erlang:trunc(N/2))),
	FragmentsIdMap = lists:map(fun(_)-> random:uniform(erlang:trunc(N/2)) end, lists:seq(1,N)),
	Pids = lists:map(fun(Node) -> spawn(gossip_fetch_i, gossip, [lists:map(fun(_) -> random:uniform(N) end, lists:seq(0,erlang:trunc(math:log(N)))), 
								N, 
								lists:nth(lists:nth(Node, FragmentsIdMap), Fragments), 
								lists:nth(Node, FragmentsIdMap), 
								[],
								awaitingInitialisation, 0, StatProcess]) 
			end, 
		lists:seq(1,N)),

	io:format("~p~n",[Pids]),
	
	FetchFragment = send_init(Pids, N),
	%FetchFragment = random:uniform(N),
	io:format("~n~n~p fetching fragment id ~p (fragment : ~p)~n", [lists:nth(1,Pids), lists:nth(1,FetchFragment), lists:nth(2,FetchFragment)]),
	lists:nth(1,Pids) ! {fetchStart, lists:nth(1,FetchFragment), lists:nth(length(Pids),Pids)}
.

statFun(FetchCount, StopCount, DataDisseminateCount, N) -> 
	receive
		{fetch} -> statFun(FetchCount+1, StopCount, DataDisseminateCount, N);
		{stopFetching} -> statFun(FetchCount, StopCount+1, DataDisseminateCount, N);
		
		{dataDisseminate} -> statFun(FetchCount, StopCount, DataDisseminateCount+1, N);
		{dataRecieved} -> io:format("~n~n~n~n -----------Messages to receipt of data ----------- ~nFetch Messages = ~p~nData Dissemination Messages = ~p~nStop Messages =~p~n~n~n~n",[FetchCount, StopCount, DataDisseminateCount]),
		statFun(FetchCount, StopCount, DataDisseminateCount+1, N);
		_ -> a
		after 5000 -> 
			io:format("~n~n~n~n ----------- Total Messages ----------- ~nFetch Messages = ~p~nData Dissemination Messages = ~p~nStop Messages =~p~n~n",[FetchCount, StopCount, DataDisseminateCount])
	end
.

send_init(Pids, N) ->
	if N>0 -> lists:nth(N,Pids) ! {init,Pids},
		%io:format("dhondu ~p initialized~n", [lists:nth(N,Pids)]),
		send_init(Pids, N-1);
	true -> 
		lists:nth(random:uniform(length(Pids)), Pids) ! {self(), tellFragmentId},
		receive
			{FragmentId, Fragment} -> [FragmentId,Fragment]
		end
	end	
.
