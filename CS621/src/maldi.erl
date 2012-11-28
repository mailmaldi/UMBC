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

generateChord(Pids) ->
	N = length(Pids),
	io:format("N ~p~n",[N]),
	LogN = getLog(N),
	io:format("LogN ~p~n",[LogN]),
	
	Dict = dict:new(),
	
	List = lists:map( fun(T) -> T end, lists:seq(1,N) ),
	%io:format("List ~p~n",[List]),

	LogL = lists:map( fun(T) -> T end, lists:seq(1,LogN) ),
	io:format("LogL ~p~n",[LogL]),	
	lists:map(fun(T) ->
					  TempList = lists:map( fun(L) -> 
													lists:nth( getIndex(T,L,N) , Pids) 
											end , LogL), 
					  %io:format("Internal:~p= ~p~n",[T,TempList]),  
					  [lists:nth(T,Pids)] ++ TempList  %% ++ lists:map( fun(_) -> lists:nth(T,Pids) end, lists:seq(1,LogN - 1))
			  end, List).
	
	



