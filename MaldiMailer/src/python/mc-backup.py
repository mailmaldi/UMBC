"""Example of Missionaries and Cannibals for use with AIMA code"""

#! /usr/bin/python

from search import *

class MC(Problem):
    """ Classic missionaries and Cannibals problem.  A state is
     represented by a three typle (M,C,B) where M, C and B are the
     number of missionaries, cannibals and boats on the left bank,
     repsectively.  A state is legal if it is not the case that there
     are missionaries on a bank that re outnumbered by canniabls.
     Actions are that one or two people can row the boat from one bank
     to the other.  The problem is to find a sequence of actions to go
     from (3,3,1) to (0,0,0)."""

    # missionary , cannibal , isBoatHere
    def __init__(self, capacities=(3,3,1), initial=(3,3,1), goal=(0,0,0)):
        self.capacities = capacities
        self.initial = initial
        self.goal = goal 

    def __repr__(self):
        """returns a string representing the object"""
        return "MC(%s,%s,%s)" % (self.capacities , self.initial , self.goal)

    def goal_test (self, state):
        """returns true if state is a goal state"""
        g = self.goal
        #print "inside goal_test state=[%s %s %s] goal=[%s %s %s]" % (state[0],state[1],state[2],g[0],g[1],g[2])
        return ( state[0] == g[0] ) and ( state[1] == g[1] ) and ( state[2] == g[2] )

    def isLegalState(self,M,C,B):
        #print "inside isLegalState M=%s C=%s B=%s self.capacities[0]=%s self.capacities[1]=%s" % (M,C,B,self.capacities[0],self.capacities[1])
        if ((M >= 0) and (M<=self.capacities[0])) and (C >= 0 and (C<=self.capacities[1])) and ((B >= 0) and (B<=1)):
            #print "basic tests are passed"
            pass
        else:
            return False
        # validate the sizes & capacities so that successor() is as small as possible
        # check if cannibals outnumber missionaries at either end
        
        #print "inside isLegalState passed basic limits, checking edibility"

        # condition is that on either side, if there is atleast 1 M, check if there are more than 1C i.e. will M be edible
        if ( (M > 0) and (C > M)) or ( ( (self.capacities[0]-M) > 0) and (self.capacities[1]- C) > (self.capacities[0]-M) ):
            return False
        #print "inside isLegalState passed edibility"
        return True

    def successor(self, (M,C,B)):
        """returns a list of successors to state"""
        #print "in successor function"
        successors = []
        (M0 , C0 , B0) = self.capacities
        if B == 1:
            # can do 1 step from 1->5 but have to check if the final state will be legal or not
            #print "just before checking for legal state"
            if self.isLegalState((M-1),C ,(B-1)):
                #print "move 1M right"
                successors.append(( 'move 1M right', ((M-1),C ,(B-1)) ))
            if self.isLegalState((M-2),C ,(B-1)):
                #print "move 2M right"
                successors.append(( 'move 2M right', ((M-2),C ,(B-1) )))
            if self.isLegalState(M,(C-1) ,(B-1)):
                #print "move 1C right"
                successors.append(( 'move 1C right', (M,(C-1) ,(B-1) )))
            if self.isLegalState(M,(C-2) ,(B-1)):
                #print "move 2C right"
                successors.append(( 'move 2C right', (M,(C-2) ,(B-1) )))
            if self.isLegalState((M-1),(C-1) ,(B-1)):
                #print "move 1M 1C right"
                successors.append(( 'move 1M 1C right', ((M-1),(C-1) ,(B-1) )))
        else:
            # can do 1 step from 6->10 but check if legal, else dont add to successor
            if self.isLegalState((M+1),C ,(B+1)):
                successors.append(( 'move 1M left', ((M+1),C ,(B+1) )))
            if self.isLegalState((M+2),C ,(B+1)):
                successors.append(( 'move 2M left', ((M+2),C ,(B+1) )))
            if self.isLegalState(M,(C+1) ,(B+1)):
                successors.append(( 'move 1C left', (M,(C+1) ,(B+1) )))
            if self.isLegalState(M,(C+2) ,(B+1)):
                successors.append(( 'move 2C left', (M,(C+2) ,(B+1) )))
            if self.isLegalState((M+1),(C+1) ,(B+1)):
                successors.append(( 'move 1M 1C left', ((M+1),(C+1) ,(B+1) )))
        #print "successors: initial: [%s,%s,%s] list: %s " % (M,C,B,successors)
        return successors

def main():
    print "In Main"
    searchers = [breadth_first_tree_search, breadth_first_graph_search, depth_first_graph_search,
                 iterative_deepening_search, depth_limited_search]
    #problems =[MC((2,2,1), (2,2,1) , (0,0,0) ), MC((4,4,1), (4,4,1) , (0,0,0) ),MC((3,3,1), (3,3,1) , (0,0,0) )]
    problems =[MC((2,2,1), (2,2,1) , (2,1,1) )]
    for p in problems:
        for s in searchers:
            print "Solution to %s found by %s" % (p, s.__name__)
            path = s(p).path()
            #print "before path.reverse()"
            path.reverse()
            print path
            print

    print "SUMMARY: successors/goal tests/states generated/solution"
    compare_searchers(problems=problems, header=['', ''], searchers=searchers)

# if called from the command line, invoke main()
if __name__ == "__main__":
    main()
