// Lab5_pdp_karatsuba.cpp : This file contains the 'main' function. Program execution begins and ends there.
//

#include <iostream>
#include <vector>
#include <thread>
#include <cstdlib>
#include <string>

const int max_th = 10;
int curr_th = 0;

void karatsubaParallel(const std::vector<int>& A, const std::vector<int>& B, std::vector<int>& res) {
	if (A.size() == 1 && B.size() == 1) {
		res[0] = A[0] * B[0];
		return;
	}

	int peJuma = A.size() / 2 + A.size() % 2;

	std::vector<int> a_low(A.begin(), A.begin() + peJuma);
	std::vector<int> a_high(A.begin() + peJuma, A.end());
	std::vector<int> b_low(B.begin(), B.begin() + peJuma);
	std::vector<int> b_high(B.begin() + peJuma, B.end());

	std::vector<int> low(a_low.size() + b_low.size() - 1);
	std::vector<int> high(a_high.size() + b_high.size() - 1);

	std::vector<std::thread> threads;

	curr_th += 2;
	if (curr_th < max_th) {
		threads.push_back(std::thread([&a_low, &b_low, &low]() {karatsubaParallel(a_low, b_low, low); }));
		threads.push_back(std::thread([&a_high, &b_high, &high]() {karatsubaParallel(a_high, b_high, high); }));
	}
	else {
		karatsubaParallel(a_low, b_low, low);
		karatsubaParallel(a_high, b_high, high);
	}

	std::vector<int> copy_a_low(a_low);
	std::vector<int> copy_b_low(b_low);

	for (int i = 0; i < a_high.size(); i++) {
		copy_a_low[i] += a_high[i];
		copy_b_low[i] += b_high[i];
	}

	std::vector<int> mid(copy_a_low.size() + copy_b_low.size() - 1);
	
	curr_th += 1;
	if (curr_th < max_th) {
		threads.push_back(std::thread([&copy_a_low, &copy_b_low, &mid]() {karatsubaParallel(copy_a_low, copy_b_low, mid); }));
	}
	else {
		karatsubaParallel(copy_a_low, copy_b_low, mid);
	}

	curr_th -= threads.size();

	for (int i = 0; i < threads.size(); i++) {
		threads[i].join();
	}

	for (int i = 0; i < low.size(); i++) {
		res[i] += low[i];
	}

	for (int i = 0; i < mid.size(); i++) {
		res[i + peJuma] += mid[i] - low[i] - high[i];
	}

	for (int i = 0; i < high.size(); i++) {
		res[i + peJuma * 2] += high[i];
	}
}

std::vector<int> createPolynomial() {
	int n;
	std::cout << "Size of the polynomial: ";
	std::cin >> n;

	std::vector<int> res;
	for (int i = 0; i < n; i++) {
		res.push_back(rand() % n);
	}
	return res;
}

void prettyPrintPolynomial(std::vector<int> poly) {
	for (int i = 0; i < poly.size(); i++) {
		std::cout << poly[i];
		if (i != 0) {
			std::cout << "X^" << i;
		}
		if (i != poly.size() - 1) {
			std::cout << " + ";
		}
	}
	std::cout << "\n";
}

std::vector<int> computePolynomial(std::vector<int> A, std::vector<int> B) {
	std::vector<int> res(A.size() + B.size() - 1);

	for (int i = 0; i < A.size(); i++) {
		for (int j = 0; j < B.size(); j++) {
			res[i + j] += A[i] * B[j];
		}
	}
	return res;
}

int main()
{
	std::vector<int> a, b;
	a = createPolynomial();
	b = createPolynomial();

	prettyPrintPolynomial(a);
	prettyPrintPolynomial(b);

	std::vector<int> poly(a.size() + b.size() - 1);

	auto start = std::chrono::steady_clock::now();
	poly = computePolynomial(a, b);
	auto end = std::chrono::steady_clock::now();

	std::chrono::duration<double> seconds = end - start;
	std::cout.precision(7);
	prettyPrintPolynomial(poly);
	std::cout << "Polynomial in sequential form: " << std::fixed << seconds.count() << "\n";
	int size_a = a.size();
	while (size_a & (size_a - 1) != 0) {
		size_a++;
	}
	a.resize(size_a);
	int size_b = b.size();
	while (size_b & (size_b - 1) != 0) {
		size_b++;
	}
	b.resize(size_b);

	std::vector<int> poly2(a.size() + b.size() - 1);
	start = std::chrono::steady_clock::now();
	karatsubaParallel(a, b, poly2);
	end = std::chrono::steady_clock::now();

	seconds = end - start;

	if (poly2.size() > poly.size()) {
		poly2.resize(poly.size());
	}
	prettyPrintPolynomial(poly2);
	std::cout << "Karatsuba in parallel form: " << std::fixed << seconds.count() << "\n";
    std::cout << "Hello World!\n";
}

// Run program: Ctrl + F5 or Debug > Start Without Debugging menu
// Debug program: F5 or Debug > Start Debugging menu

// Tips for Getting Started: 
//   1. Use the Solution Explorer window to add/manage files
//   2. Use the Team Explorer window to connect to source control
//   3. Use the Output window to see build output and other messages
//   4. Use the Error List window to view errors
//   5. Go to Project > Add New Item to create new code files, or Project > Add Existing Item to add existing code files to the project
//   6. In the future, to open this project again, go to File > Open > Project and select the .sln file
