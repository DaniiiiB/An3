% System of congruences

Batincu Daniel, group 931

The purpose of this program is to solve the system of congruences. A system may look like this:

$x \equiv b_1 \pmod{m_1}$

$x \equiv b_2 \pmod{m_2}$

$...$

$x \equiv b_r \pmod {m_r}$

Solutions to this system must be between 0 and $m-1$, where $m=m_1*m_2*...*m_r$

We have 2 cases for this problem:

The first case is when all the moduli are $pairwise coprime$

Proposition

: A list of natural numbers ${n_1,n_2,...,n_r}$ is considered to be pairwise coprime if

$gcd(n_i,n_j) = 1$  $\forall i,j \in {1,2,...r}, i \ne j$

# The first case

In this case we use the Chinese Remainder Theorem.

Chinese Remainder Theorem

:Consider a system of congruences with pairwise coprime moduli. The system has a unique solution modulo $N = n_1*n_2*...*n_r$, namely:

$x = \sum_{i=1}^{r} b_i N_i K_i$,

where $N_i=N/n_i$, $K_i=N_i^{-1} \pmod{n_i}$

To check if the moduli are pairwise coprime, we check every pair of moduli and check if every pair is coprime, i.e. the greatest common divisor between them is 1.

~~~~~{.python}
<< Coprime check >>=
def gcd(x, y):  
    	while(y): 
        	x, y = y, x % y  
    	return x 

def pairCoprime(pairs):
	for i in range(len(pairs) - 1):
		for j in range(i + 1, len(pairs)):
			if gcd(pairs[i]['m'],pairs[j]['m']) != 1:
				return False
	return True
@
~~~~~

The approach made here is taking each equation and substituting the value of x, i.e. 

$x \equiv b_1 \pmod{m_1}$ (1)

$x \equiv b_2 \pmod{m_2}$ (2)

From (1) we can write $x = m_1*j + b_1$, for an integer $j$. We then subtitute this result in (2), and we get:

$m_1*j + b_1 \equiv b_2 \pmod{m_2}$, or $m_1*j \equiv b_2-b_1 \pmod{m_2}$


To get the value of j from this qeuation, we use this function:

~~~~~{.python}
<< Congruence equation >>=
def prodMod(n,b,m):
	# nk % m == b   nk = b (mod m)
	for k in range(m):
		if (n * k) % m == b:
			return k
@
~~~~~

The solution will be of the form $j = n*k + b$ 

This result we put into x, and we keep going (we substitute x from the next equation with this result value), until we reach something like:

$x = (m_1*m_2*...*m_r)*l + B$,

and our result is the value of B.

And the algorithm is:

~~~~~{.python}
<< Chinese >>=
<< Congruence equation >>
def coprimeChinese(pairs):
	m1 = pairs[0]['m']
	b1 = pairs[0]['b']
	for i in range (1,len(pairs)):
		m2 = pairs[i]['m']
		b2 = pairs[i]['b']
		j = prodMod(m1,(b2-b1) % m2,m2)
		b1 = (m1 * j + b1) % (m1 * m2)
		m1 = m1 * m2
	return [b1]
@


~~~~~

# The second case

If the moduli are not pairwise coprime, every equation needs to be split into multiple euqations for every prime factor of the modulus.

~~~~~{.python}
<< Prime Factors >>=
def primeFactors(n):
    	i = 2
    	factors = []
    	while i * i <= n:
        	if n % i:
            		i += 1
        	else:
			nr = 1
			while n % i == 0:
            			n //= i
				nr *= i
            		factors.append(nr)
    	if n > 1:
        	factors.append(n)
    	return factors
@
~~~~~

In this case, the system has a solution if there are not any contradictions, for example $x \equiv 1 \pmod{3}$ and $x \equiv 2 \pmod{3}$. In this case, we split the equations into the ones corresponding to their prime factors:

~~~~~{.python}
<< One more step to Chinese >>=
<< Prime Factors >>
def makeExtendedPairs(pairs):
	extendedPairs = []
	for pair in pairs:
		m = pair['m']
		b = pair['b']
		factors = primeFactors(m)
		for factor in factors:
			toAdd = {'b': b % factor, 'm': factor}
			if toAdd not in extendedPairs:
				for ep in extendedPairs:
					if ep['m'] == toAdd['m'] and ep['b'] != toAdd['b']:
						return []
				extendedPairs.append(toAdd)
	return extendedPairs
@
~~~~~
	
and we apply The Chinese Remainder Theorem to the new set of equations

~~~~~{.python}
<< test >>=
<< Chinese >>
def test(pairs,l,i):
	if i == len(pairs):
		test_pair = {'b': 0, 'm':0}
		test_pairs = []
		for k in range(len(l)):
			test_pair['b'] = l[k]
			test_pair['m'] = pairs[k]['m']
			test_pairs.append(test_pair)
		if len(coprimeChinese(test_pairs)) != 1:
			return False
		l = []
		i = 0
	for j in range(pairs[i]['b']):
		l.append(j)
		return test(pairs,l,i+1)
@
~~~~~

~~~~~{.python}
<<*>>=
import sys
<< Coprime check >>
<< Chinese >>
<< One more step to Chinese >>
<< test >>
#B = list(map(int, sys.argv[1].strip('[]').split(',')))
#Q = list(map(int, sys.argv[2].strip('[]').split(',')))
#print(B)
#print(Q)
lists = input()
b_list, m_list = lists.split()
B = list(map(int, b_list.strip('[]').split(',')))
M = list(map(int, m_list.strip('[]').split(',')))
pairs = []
for i in range(len(B)):
	pair = {'b': B[i], 'm': M[i]}
	pairs.append(pair)
if pairCoprime(pairs):
	print(coprimeChinese(pairs))
	print(test(pairs,[],1))
else:
	pairs = makeExtendedPairs(pairs)
	if len(pairs) == 0:
		print("Unsolvable")
	else:
		print(coprimeChinese(pairs))



@
~~~~~
