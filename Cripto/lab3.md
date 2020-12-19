% Pollard's p-1 algorithm

Batincu Daniel, group 931

The purpose of this program is to find a non-trivial factor p of an odd composite number n for which p-1 has only small prime divisors.

So, we are able to find a multiple k of p-1 without actually computing p-1, as a product of small primes.

By Fermat’s Little Theorem, $a^k \equiv 1 \pmod(p)$, $\forall a \in Z$, with $p \nmid a$. Then $p \mid a^k-1$. If $n \nmid a^k-1$, then $d = (a^k-1,n)$ is a non-trivial factor of n.

The algorithm fails if $d=1$ or $d=n$.

In this implementation the candidate for $k$ will be $k = lcm(1,...,B)$. If the primes dividing $p-1$ are smaller than $B$, then $k$ is a multiple of $p-1$.


For our algorithm we will need to compute the modulo of an exponentiation, so for this we will need the Modular Squaring Exponentiation Algorithm, which takes the remainder modulo $modulo$ every time we raise the result to a power.

~~~~~{.python}
<< Modular Square Exponent >>=
def exponentiation(base, exp, modulo): 
    if (exp == 0): 
        return 1; 
    if (exp == 1): 
        return base % modulo; 
      
    t = exponentiation(base, int(exp / 2), modulo); 
    t = (t * t) % modulo; 
      
    # if exponent is even value 
    if (exp % 2 == 0): 
        return t; 
          
    # if exponent is odd value 
    else: 
        return ((base % modulo) * t) % modulo;

@
~~~~~

In order to compute the value of k, we will need to compute the Least Common Multiple of a set of numbers. One method will be to compute the LCM of first 2 elements of the set, then the LCM of the result with the third element and so on. To compute the LCM of 2 numbers we use this formula:

$lcm(a,b) = \frac{a*b}{gcd(a,b)}$

~~~~~{.python}
<< GCD >>=
def gcd(x, y):
	while(y):
		x,y = y, x % y
	return x

@
~~~~~

~~~~~{.python}
<< LCM of list >>=
<< GCD >>
def lcm(list):
	lcm = list[0]
	for i in list[1:]:
		lcm = lcm * i // gcd(lcm, i)
	return lcm

@
~~~~~


This algorithm runs for values $1 < a < n-1$, but it will stop if the algorithm succeds for a value a.
The first step if to compute the value $a^k \pmod{n}$.
Afterwards, it will compute the GCD between the previous result after subtracting 1 (i.e. $A-1$).
If the value of the previous result d is equal to 1 or n, the algorithm fails. If this happens, we choose the next value of a.

~~~~~{.python}
<< Pollard's p-1 >>=
<< LCM of list >>
<< Modular Square Exponent >>
def pollard(n,B=15):
	b = range(1,B+1)
	k = lcm(b)
	for a in range(2,n-1):
		#if a > 15 and a % 50 == 0 or a <= 5:
		#	print("Trying a = " + str(a))
		A = exponentiation(a,k,n)
		d = gcd(A-1,n)
		if d != 1 and d != n:
		#	print("Success with a = " + str(a))
			return d
		#if a > 15 and a % 50 == 0 or a <= 5:
		#	print("Failed!")
	return -1

@
~~~~~

The method I chose to test the algorithm is to generate pairs of prime numbers, multiply them and check if the result is one of the numbers. If I were to chose non-prime numbers, the algorithm might give another number that is harder to check because it may contain factors from both of the given numbers.

To do my testing, I created a function that geenrates the next prime number after a given number.

~~~~~{.python}
<< Next Prime >>=
import math
def isPrime(n):   
    if(n <= 1): 
        return False
    if(n <= 3): 
        return True  
    if(n % 2 == 0 or n % 3 == 0): 
        return False
      
    for i in range(5,int(math.sqrt(n) + 1), 6):  
        if(n % i == 0 or n % (i + 2) == 0): 
            return False      
    return True
    

def nextPrime(N): 
  
    # Base case  
    if (N <= 1): 
        return 2
  
    prime = N 
    found = False
    
    while(not found): 
        prime = prime + 1
  
        if(isPrime(prime) == True): 
            found = True
  
    return prime 

@
~~~~~


~~~~~{.python}
<<*>>=
<< Pollard's p-1 >>
<< Next Prime >>
B = input("Your bound(leave empty for default bound B = 15): ")
test = input("Do you want to perform tests or run the algorithm for a number of your choice? (type \"test\" for a test, or your number to run the algorithm on your number): ")
if B == "":
	B = 15
else:
	B = int(B)
if test == "test":
	#a,b = 5477,22697
	a,b = 13,23
	for i in range(100):
		n = a*b
		print("a: " + str(a) + "   b: " + str(b))
		d = pollard(n,B)
		result = (d==a or d==b)
		print("a: " + str(a) + "   b: " + str(b) + "   pollard's p-1 result: " + str(d) + "   " + str(result))
		a = nextPrime(a)
		b = nextPrime(nextPrime(b))
		print("")

else:
	print("n: " + str(test))
	print(pollard(int(test),B))

@
~~~~~

HOW TO RUN THE PROGRAM:

1. notangle lab3.md > lab3.py
2. python3 lab3.py / python lab3.py
3. Input
4. Let the program do its thing and enjoy :)
