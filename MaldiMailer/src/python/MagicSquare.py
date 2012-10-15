from constraint import *
import time
import sys

def createMagicSquare(squareSize):
    problem = Problem()
##        problem.setSolver(self.solver)
    
    
    problem= Problem(BacktrackingSolver())
    
    magicSum = squareSize*(((squareSize*squareSize)+1)/2)
    elements = squareSize*squareSize
    
    problem.addVariables(range(0,elements), range(1, elements+1))
    problem.addConstraint(AllDifferentConstraint(), range(0, elements))
    
    
    diagonal = []
    for i in range(squareSize):
        diagonal.append(0 + (squareSize * i + i))
        
    diagonal2 = []
    for i in range(squareSize):
        diagonal2.append((squareSize * (i + 1) - (i + 1))) #[3, 6, 9, 12] [2,4,6] [4,8,12,16,20]
    
    problem.addConstraint(ExactSumConstraint(magicSum),diagonal)
    
    problem.addConstraint(ExactSumConstraint(magicSum),diagonal2)
    
    for row in range(squareSize):
        problem.addConstraint(ExactSumConstraint(magicSum),
                              [row*squareSize+i for i in range(squareSize)])
    for col in range(squareSize):
        problem.addConstraint(ExactSumConstraint(magicSum),
                              [col+squareSize*i for i in range(squareSize)])

    ##backtracking solver
        
    
    
    start = time.time()
    solution = problem.getSolution()
    
    stop = time.time()
    
    print 'Solution using BacktrackingSolver()...time taken %.3f seconds' % (stop-start)
    print '\n'
    
    #for s in solution:
    for row in range(squareSize):
        for col in range(squareSize):
            print solution[row*squareSize+col],
        print
    print
    print


    ##recursive backtracking

    problem= Problem(RecursiveBacktrackingSolver())

    start = time.time()
    solution = problem.getSolution()
    stop = time.time()

    print 'Solution using RecursiveBacktrackingSolver()...time taken %.3f seconds' % (stop-start)
    print '\n'
    

    #for s in solution:
    for row in range(squareSize):
        for col in range(squareSize):
            print solution[row*squareSize+col],
        print
    print
    print
        
    ##MinConflictsSolver()

    problem= Problem(MinConflictsSolver())

    start = time.time()
    solution = problem.getSolution()
    stop = time.time()

    print 'Solution using MinConflictsSolver()...time taken %.3f seconds' % (stop-start)
    print '\n'
    print solution
    
##        for s in solution:
##            for row in range(3):
##                for col in range(3):
##                    print s[row*3+col],
##            print


def main():
    createMagicSquare(4)
    #createMagicSquare(4)

if __name__ == "__main__":

    # 1st command line arg is always the command name (e.g., my.py)
    nargs = len(sys.argv)

    # Note that we call eval on any command line arguments to convert
    # the string into a python data structure

    if nargs > 3:
        print "Usage: python mc.py [initial_state [goal_state]]"
    elif nargs == 3:
        main(initial=eval(sys.argv[1]), goal=eval(sys.argv[2]))
    elif nargs == 2:
        main(initial=eval(sys.argv[1]))
    else:
        main()
