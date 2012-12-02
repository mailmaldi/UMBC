%% Author: Milind
%% Created: Nov 22, 2012
%% Description: TODO: Add description to useless
-module(maldi).
-compile([debug_info, export_all]).



%% To Generate the Topology of the graph
generateTopology(Pids, Flag) ->
	case(Flag) of
		1 -> 
			%io:format("~p Pids In this Ring Topology~n",[Pids]),
			N = length(Pids),
			TempPids = Pids ++ Pids,
			Topology = lists:map(fun(T) -> [lists:nth(N+(T-1),TempPids),lists:nth(N+1+(T-1),TempPids)] end, lists:seq(1,N));
			%io:format("Ring Topology~n~p",[Topology]);
		2 ->
			%io:format("~p Pids In this Expander Graph Topology connected to 3 nodes(Degree=3)~n",[Pids]),
			N = length(Pids),
			TempPids = Pids ++ Pids ++ Pids,
			Topology = lists:map(fun(T) -> [lists:nth(N+(T-1),TempPids),lists:nth(N+1+(T-1),TempPids),lists:nth(N+2+(T-1),TempPids)] end, lists:seq(1,N));
			%io:format("Expander Graph with Degree = 3~n~p",[Topology]);
		3 ->
			%io:format("~p Pids In this Expander Graph Topology connected to 4 nodes(Degree = 4)~n",[Pids]),
			N = length(Pids),
			TempPids = Pids ++ Pids ++ Pids ++ Pids,
			Topology = lists:map(fun(T) -> [lists:nth(N+(T-1),TempPids),lists:nth(N+1+(T-1),TempPids),lists:nth(N+2+(T-1),TempPids),lists:nth(N+3+(T-1),TempPids)] end, lists:seq(1,N));
			%io:format("Expander Graph with Degree = 4~n~p",[Topology]);
		4 -> 
			Topology = generateChord(Pids);
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

%% specialized 2 functions when my Dict stores <K,V> = <FragmentID, {SUM,COUNT} >
dictToSum(Dict) ->
	dict:fold(fun(Key,Val,Acc) -> Acc + element(1,Val) end, 0,Dict).

dictToCount(Dict) ->
	dict:fold(fun(Key,Val,Acc) -> Acc + element(2,Val) end, 0,Dict).

%% Dict1 is incoming, Dict2 is existing, wish to return only those unique in Dict2
%% Should reduce n/w b/w a lot hopefully?
dictSubtract(Dict2,Dict1) ->
	Dict2KeySet = ordsets:from_list(dict:fetch_keys(Dict2)),
	Dict1KeySet = ordsets:from_list(dict:fetch_keys(Dict1)),
	DiffKeySet = ordsets:subtract(Dict2KeySet,Dict1KeySet),
	DiffList = ordsets:to_list(DiffKeySet),
	
	TempDict = dict:new(),
	
	DiffDict = lists:foldl( 
				 fun(X, ACC) -> 
						 dict:store(X, dict:fetch(X,Dict2) , ACC) 
				 end, 
				 TempDict, 
				 DiffList
			   ),	
	DiffDict.
	%dict:to_list(DiffDict).

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
	TEST = (X - T),
    if
        TEST < 0 -> T - 1;
        TEST > 0 -> T;
        true -> T
    end.

ceiling(X) ->
    T = erlang:trunc(X),
	TEST = (X - T),
    if 
        TEST < 0 -> T;
        TEST > 0 -> T + 1;
        true -> T
    end.

log2(N) ->
	math:log10(N)/ math:log10(2).


getData(N) ->
	Values = lists:map(fun(_) -> lists:map( fun(_) -> random:uniform(1000)+0.5 end  , lists:seq(1,10)) end, lists:seq(1,N)),
	FlatList = lists:flatten(Values),
	io:format("~n~nMALDI: MIN=~p MAX=~p AVERAGE=~p MEDIAN=~p~n~n",[lists:min(FlatList),lists:max(FlatList),mean(FlatList),median(FlatList)]),
	Values.

getLog(N) ->
	ceiling(log2(N)).

getList(N) ->
	List = lists:map(fun(T) -> T+0.5 end, lists:seq(1,N)),
	%io:format("List ~p~n",[List]),
	List.

getIndex(T,L,N) ->
	Index = (T + ceiling(math:pow(2,L-1))) rem N,
	if
		Index =< 0 ->
			RETURN = 1;
		Index > N ->
			RETURN = N;
		true ->
			RETURN = Index
	end,
	RETURN.


%% Milind - The following generates a uni-directional chord, every node knows itself,+1,+2,+4,+8.... total logN nodes, but not guaranteed to be vice-versa.
%% It still generates a fully-connected graph.
generateChord(Pids) ->
	N = length(Pids),
	io:format("N ~p~n",[N]),
	LogN = getLog(N)-1, %% milind -> floor instead of ceiling, maybe partition size is smaller? wanna try 
	io:format("LogN ~p~n",[LogN]),
	
	List = lists:map( fun(T) -> T end, lists:seq(1,N) ),
	%io:format("List ~p~n",[List]),

	LogL = lists:map( fun(T) -> T end, lists:seq(1,LogN) ),
	io:format("LogL ~p~n",[LogL]),	
	lists:map(fun(T) ->
					  TempList = lists:map( fun(L) -> 
													lists:nth( getIndex(T,L,N) , Pids) 
											end , LogL), 
					  %io:format("Internal:~p= ~p~n",[T,TempList]),  
					  [lists:nth(T,Pids)] ++ TempList  ++ lists:map( fun(_) -> lists:nth(T,Pids) end, lists:seq(1,LogN - 1)) %% This makes logN entries of same node & logN others
			  end, List).


%% Milind -> Common send PULL code for all files.
sendPull(Neighbours_list,Fragment_Id) ->
	{A1,A2,A3} = now(), 
    random:seed(A1, A2, A3),
	if 
		length(Neighbours_list) > 0 ->
			NewNodePID2 = lists:nth(random:uniform(length(Neighbours_list)), Neighbours_list) ,
				if 
							self() == NewNodePID2 ->
								%io:format("~p PULL SELF, just sleep ~n",[self()]);
								K = 0;
							true ->
								PULL_ID=maldi:getRandomNumber()+Fragment_Id,
								%io:format("~p pull TO ~p PULL_ID ~p~n",[self(),NewNodePID2,PULL_ID]),
								NewNodePID2 ! {pull_request,self(),PULL_ID},
								K = 1
				end;
		true ->
			K = 0
	end,
	{ K , maldi:getCurrentTS()}.