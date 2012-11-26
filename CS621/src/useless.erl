%% Author: Milind
%% Created: Nov 22, 2012
%% Description: TODO: Add description to useless
-module(useless).
-compile([debug_info, export_all]).
-export([add/2, hello/0, greet_and_add_two/1]).
 
add(A,B) ->
A + B.
 
%% Shows greetings.
%% io:format/1 is the standard function used to output text.
hello() ->
io:format("Hello, world!~n").
 
greet_and_add_two(X) ->
hello(),
add(X,2).

heh_fine() ->
if 1 =:= 1 ->
works
end,
if 1 =:= 2; 1 =:= 1 ->
works
end,
if 1 =:= 2, 1 =:= 1 ->
fails;
true -> alwaysFail
end.

head([H|_]) -> H.

second([_,X|_]) -> X.

same(X,X) ->
true;
same(_,_) ->
false.

fac(0) -> 1;
fac(N) -> N*fac(N-1).

tail_fac(N) -> tail_fac(N,1). 
tail_fac(0,Acc) -> Acc;
tail_fac(N,Acc) when N > 0 -> tail_fac(N-1,N*Acc).

len([]) -> 0;
len([_|T]) -> 1 + len(T).

tail_len(L) -> tail_len(L,0).
 
tail_len([], Acc) -> Acc;
tail_len([_|T], Acc) -> tail_len(T,Acc+1).

%tail recursion example for duplicating a term
duplicate(0,_) -> [];
duplicate(N,Term) when N > 0 -> [Term|duplicate(N-1,Term)].

tail_duplicate(N,Term) -> tail_duplicate(N,Term,[]).
tail_duplicate(0,_,List) -> List;
tail_duplicate(N,Term,List) when N > 0 -> tail_duplicate(N-1, Term, [Term|List]).

dolphin1() ->
receive
do_a_flip ->
io:format("How about no?~n");
fish ->
io:format("So long and thanks for all the fish!~n");
_ ->
io:format("Heh, we're smarter than you humans.~n")
end.

dolphin2() ->
io:format("Welcome to the world of Dolphins.~n"),
receive
{From, do_a_flip} ->
From ! "How about no?",
dolphin2();
{From, fish} ->
From ! "So long and thanks for all the fish!";
_ ->
timer:sleep(1000),
io:format("Heh, we're smarter than you humans.~n"),
dolphin2()
end.

gets(_,0)-> io:format("REACHED END of ~p~n",[self()]);
gets(N, K) ->
SeedValue = 1.5,
%%Values = lists:map(fun(T) -> [SeedValue+T+100] end, lists:seq(1, N)),
Values = lists:map(fun(T) -> [SeedValue + T , SeedValue + T +1] end, lists:seq(1, N)), % foreach 1,2,3,4...N
lists:foreach(fun(T) -> io:format("index ~p value ~p sum ~p~n",[T,lists:nth(T,Values),lists:sum(lists:nth(T,Values))]) end , lists:seq(1,N)),  %% io:format("~p~n~n~n",[Values]).
io:format("REACHED END of ~p~n", [self()]),
gets(N,K-1).

test_random() ->
	List = [],
	Index = random:uniform(length(List)), 
	lists:nth(Index, List). 

getMinimum(MIN1,MIN2) ->
	if
				MIN1 < MIN2 ->
					NEW_MIN = MIN1;
				MIN2 < MIN1 ->
					NEW_MIN = MIN2;
				MIN1 == MIN2 ->
					NEW_MIN = MIN1
			end.

getMaximum(MAX1,MAX2) ->
	if
				MAX1 > MAX2 ->
					NEW_MAX = MAX1;
				MAX2 > MAX1 ->
					NEW_MAX = MAX2;
				MAX1 == MAX2 ->
					NEW_MAX = MAX1
			end.

testReceive() ->
	io:format("1. testReceive INVOKED~n"),
	timer:sleep(10000),
	io:format("2. testReceive AFTER SLEEP~n"),
	receive
				_ -> 
					io:format("3. testReceive RECEIVED MESSAGE")
	end.

testMerge() ->
	Dict = dict:new(),
	Dict1 = dict:store(1,[1,2,3,4,5],Dict),
	Dict2 = dict:store(2,[1,2,3],Dict1),
	Dict3 = dict:store(1,[1.5,2.5,3.5],Dict),
	Dict4 = dict:store(3,[1.5],Dict3),
	DataDict = dict:merge(fun(_,V1,V2) -> V2 end,Dict2,Dict4),
	DataDict.
	
testSumDict() ->
	MyDict = testMerge(),
	io:format("~p Dict~n",[dict:to_list(MyDict)]),
	List = dict:fold(fun(Key,Val,Acc) -> Acc++Val end, [],MyDict),
	io:format("~p List~n~p Sorted~n~n",[List,lists:sort(List)]),
	io:format("MAX=~p MIN=~p AVERAGE=~p MEDIAN=~p ~n~n~n",[lists:max(List), lists:min(List), lists:sum(List)/length(List) , median(List)]).


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

%% check if a number is even or odd
even_or_odd(Num) when is_integer(Num) ->
    if
        Num band 1 == 0 -> even;
        true            -> odd
    end.

testPrint() ->
	List = lists:seq(1,10),
	lists:foreach( fun(T) -> io:format("~p veeresh says ~n",[T]) end , List).

% gets a timestamp in ms from the epoch
get_timestamp() ->
    {Mega,Sec,Micro} = erlang:now(),
    (Mega*1000000+Sec)*1000000+Micro.



