using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace Lab5_PDP
{
    class Program
    {
        static int[] createPoly()
        {
            Random random = new Random();

            Console.Write("Dimension of the polynomial = ");
            int n = Int32.Parse(Console.ReadLine());
            int[] poly = new int[n];

            for(int i = 0; i < n; i++)
            {
                poly[i] = random.Next(n);
            }
            return poly;
        }

        //------------------------------------------------------------------
        static int[] computePolynomial(ref int[] A, ref int[] B)
        {
            DateTime time = DateTime.Now;

            int[] prod = new int[A.Length + B.Length - 1];

            for(int i = 0; i < A.Length; i++)
            {
                for (int j = 0; j < B.Length; j++)
                {
                    prod[i + j] += A[i] * B[j];
                }
            }

            DateTime time2 = DateTime.Now;
            TimeSpan ts = time2 - time;

            prettyPrintPoly(prod);
            Console.WriteLine("Time to execute in sequential form: " + ts);
            return prod;
        }

        static void computePolynomialParallel(object arg)
        {
            Array array = new object[5];
            array = (Array)arg;
            int[] A = (int[])array.GetValue(0);
            int[] B = (int[])array.GetValue(1);
            int[] res = (int[])array.GetValue(2);
            int start = (int)array.GetValue(3);
            int stop = (int)array.GetValue(4);
            for (int i = 0; i < A.Length; i++)
            {
                for(int j = start; j < stop; j++)
                {
                    Interlocked.Add(ref res[i + j], A[i] * B[j]);
                }
            }
        }

        static int[] polynomialParallel(int[] A, int[] B, int[] res, int nrThreads)
        {

            int start = 0;
            int stop = 0;
            int perThread = B.Length / nrThreads;
            int rem = B.Length % nrThreads;

            Thread[] threads = new Thread[nrThreads];

            DateTime time = DateTime.Now;
            for (int index = 0; index < nrThreads; index++)
            {
                stop = start + perThread;
                if (rem != 0)
                {
                    stop += 1;
                    rem -= 1;
                }
                object args = new object[5] { A, B, res, start, stop };
                threads[index] = new Thread(() =>
                    computePolynomialParallel(args));
                threads[index].Start();
                start = stop;
            }

            for(int index = 0; index < nrThreads; index++)
            {
                threads[index].Join();
            }

            DateTime time2 = DateTime.Now;
            TimeSpan ts = time2 - time;

            prettyPrintPoly(res);


            Console.WriteLine("Time to execute in parallel form: " + ts);
            return res;
        }
        //-------------------------------------------------------------------------------

        static void karatsuba(ref int[] A, ref int[] B, ref int[] res)
        {
            if (A.Length == 1 && B.Length == 1)
            {
                res[0] = A[0] * B[0];
                return;
            }

            int peJuma = A.Length / 2 + A.Length % 2;

            //pregatire
            int[] a_low = new int[peJuma];
            Array.Copy(A, 0, a_low, 0, peJuma);
            int[] a_high = new int[peJuma];
            int[] b_low = new int[peJuma];
            Array.Copy(B, 0, b_low, 0, peJuma);
            int[] b_high = new int[peJuma];

            for (int i = 0; i < peJuma; i++)
            {
                a_high[i] = A[peJuma + i];
                b_high[i] = B[peJuma + i];
            }

            int[] low = new int[a_low.Length + b_low.Length - 1];
            int[] high = new int[a_high.Length + b_high.Length - 1];

            karatsuba(ref a_low, ref b_low, ref low);
            karatsuba(ref a_high, ref b_high, ref high);
            
            //copiez lowul
            for (int i = 0; i < low.Length; i++)
            {
                res[i] += low[i];
            }

            //fac midul
            for (int i = 0; i < a_high.Length; i++)
            {
                a_low[i] += a_high[i];
                b_low[i] += b_high[i];
            }

            int[] mid = new int[a_low.Length + b_low.Length - 1];
            karatsuba(ref a_low, ref b_low, ref mid);

            for (int i = 0; i < mid.Length; ++i)
            {
                res[i + peJuma] += mid[i] - low[i] - high[i];
            }

            for (int i = 0; i < high.Length; ++i)
            {
                res[i + peJuma * 2] += high[i];
            }
        }

        //-------------------------------------------------------------------------------

        static void prettyPrintPoly(int[] poly)
        {
            for (int i = 0; i < poly.Length; i++)
            {
                Console.Write(poly[i]);
                if (i != 0)
                {
                    Console.Write("X^" + i);
                }
                if (i != poly.Length - 1)
                {
                    Console.Write(" + ");
                }
            }
            Console.Write("\n");
        }

        static void Main(string[] args)
        {
            int[] A = createPoly();
            int[] B = createPoly();

            prettyPrintPoly(A);
            prettyPrintPoly(B);

            //sequential polynomial O(n^2)
            int[] poly = computePolynomial(ref A, ref B);
            
            //parallel polynomial O(n^2)
            int[] poly2 = new int[A.Length + B.Length - 1];
            poly2 = polynomialParallel(A, B, poly2, 4);

            //sequential karatsuba
            while ((A.Length & (A.Length - 1)) != 0)
            {
                Array.Resize(ref A, A.Length + 1);
            }
            while ((B.Length & (B.Length - 1)) != 0)
            {
                Array.Resize(ref B, B.Length + 1);
            }
            DateTime time = DateTime.Now;
            int[] poly3 = new int[A.Length + B.Length - 1];
            karatsuba(ref A, ref B, ref poly3);
            DateTime time2 = DateTime.Now;
            TimeSpan ts = time2 - time;
            if (poly3[poly3.Length - 2] == 0 && poly3[poly3.Length - 1] == 0 && poly3.Length > poly.Length)
            {
                Array.Resize(ref poly3, poly.Length);
            }
            prettyPrintPoly(poly3);


            Console.WriteLine("Time to execute in sequential form karatsuba: " + ts);
            Console.Read();
        }
    }
}
