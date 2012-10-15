from constraint import *
import time
import signal
import sys
from problem import *

def magic_square(size, timeout, problem):
 
    #problem = ProblemTimer()
    problem.addVariables(range(0, size * size), range(1, size * size + 1))
    problem.addConstraint(AllDifferentConstraint(), range(0, size * size))
    
    magic_number = (size * (size * size + 1)) / 2
    
    diagonal = []
    for i in range(size):
        diagonal.append(0 + (size * i + i))
        
    diagonal2 = []
    for i in range(size):
        diagonal2.append((size * (i + 1) - (i + 1))) #[3, 6, 9, 12] [2,4,6] [4,8,12,16,20]
    
    problem.addConstraint(ExactSumConstraint(magic_number), diagonal)
    
    problem.addConstraint(ExactSumConstraint(magic_number), diagonal2)
    
    for row in range(size):
        problem.addConstraint(ExactSumConstraint(magic_number),
                              [row * size + i for i in range(size)])
    for col in range(size):
        problem.addConstraint(ExactSumConstraint(magic_number),
                              [col + size * i for i in range(size)])
    
    #return problem.getSolutionWithTimeout(timeout)
    return problem.getSolution()


def print_solution(solutions, size):
         #print solutions
    if solutions != None :
        string = ""
        for row in range(size): 
            for col in range(size):
                element = solutions[row * size + col]
                append = " "
                if(element < 10):
                    append = "  "
                    
                string += str(element) + append
            string += "\n"
        print string
    else:
        print "no solution found"

def print_table(staticTableData):
    print "\tSIZE\tBacktrackingSolver\tRecursiveBacktrackingSolver\tMinConflictsSolver"
    string = ""
    for i in range(len(staticTableData)):
        string += "\t" + str(staticTableData[i])
        if (i + 1) % 4 == 0:
            string += "\n"
            print string
            string = ""
    
    

def main():
    
    sizes = [3, 4, 5]
    timeout = 30
    
    #problems = [ProblemTimer(BacktrackingSolver())]
    
    table_data = []
    
    for size in sizes:
        problems = [ProblemTimer(BacktrackingSolver()), ProblemTimer(RecursiveBacktrackingSolver()), ProblemTimer(MinConflictsSolver())]
        table_data.append(str(size)) 
        for problem in problems:
            start = time.time()
            solutions = magic_square(size, timeout, problem)
            stop = time.time()
            timeTaken = stop - start
            print "Calling for size %d and algo %s took %.3f seconds." % (size, problem.getSolver(), timeTaken)
            print_solution(solutions, size)
            table_data.append((str(timeTaken) + " (ns)") if solutions == None else (str(timeTaken) + " (ok)"))
    
    print_table(table_data)
            
        


"""# If called from the command line, invoke main()
if __name__ == "__main__":
        # 1st command line arg is always the command name (e.g., ms.py)
    nargs = len(sys.argv)
    #print "Number arguments = %s" % nargs

    # Note that we call eval on any command line arguments to convert
    # the string into a python data structure

    if nargs > 3:
        print "Usage: python ms.py [size [timeout]]"
    elif nargs == 3:
        main(eval(sys.argv[1]), eval(sys.argv[2]))
    elif nargs == 2:
        main(eval(sys.argv[1]))
    else:
        main()"""
if __name__ == "__main__":
    main()
