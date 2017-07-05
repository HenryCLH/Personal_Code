#include <iostream>

using std::cout;
using std::endl;

void print(int* num, int length) {
	for (int i = 0; i < length; i++) {
		cout << num[i] << " ";
	}
	cout << endl;
}

//O(n) ~ O(n^2) stable
void insertion_sort(int* num, int length) {
	for (int i = 1; i < length; i++) {
		int key = num[i];
		int j = i;
		while ((j > 0) && (num[j - 1] > key)) {
			num[j] = num[j - 1];
			j--;
		}
		num[j] = key;
	}
}

//O(n) ~ O(n^2) stable
void bubble_sort(int* num, int length) {
	for (int i = 1; i < length; i++) {
		bool is_swap = false;
		for (int j = 0; j < length - i; j++) {
			if (num[j] > num[j + 1]) {
				int tmp = num[j];
				num[j] = num[j + 1];
				num[j + 1] = tmp;
				is_swap = true;
			}
		}
		if (!is_swap) break;
	}
}

//O(n^2) unstable
void select_sort(int* num, int length) {
	int minkey;
	for (int i = 0; i < length - 1; i++) {
		minkey = i;
		for (int j = i + 1; j < length; j++) {
			if (num[j] < num[minkey]) {
				minkey = j;
			}
		}
		int tmp = num[i];
		num[i] = num[minkey];
		num[minkey] = tmp;
	}
}

//O(nlgn) unstable
void max_heapify(int* num, int begin, int end) {
	int s = begin;
	int child = begin * 2 + 1;
	while (child < end) {
		if (child + 1 < end && num[child] < num[child + 1]) {
			child++;
		}
		if (num[child] > num[s]) {
			int tmp = num[s];
			num[s] = num[child];
			num[child] = tmp;
			s = child;
			child = s * 2 + 1;
		}
		else break;
		print(num, end);
	}

}
void build_heap(int* num, int length) {
	for (int i = length / 2 - 1; i >= 0; i--) {
		max_heapify(num, i, length);
	}
}
void heap_sort(int* num, int length) {
	build_heap(num, length);
	for (int i = 0; i < length - 1; i++) {
		int tmp = num[0];
		num[0] = num[length - i - 1];
		num[length - i - 1] = tmp;
		max_heapify(num, 0, length - i - 1);
	}
}

//O(nlgn) ~ O(n^2) unstable
void quick_sort(int* num, int begin, int end) {
	int tmp = num[end];
	int left = begin;
	int right = end;
	while (true) {
		while ((num[left] < tmp) && (left < right)) left++;
		if (left == right) {
			num[left] = tmp;
			break;
		}
		else {
			num[right] = num[left];
		}
		while ((num[right] >= tmp) && (left < right)) right--;
		if (left == right) {
			num[left] = tmp;
			break;
		}
		else {
			num[left] = num[right];
		}
	}
	print(num, 10);
	if (begin < left - 1) {
		quick_sort(num, begin, left - 1);
	}
	if (left + 1 < end) {
		quick_sort(num, left + 1, end);
	}
}

//O(nlgn) stable
void merge(int* num1, int length1, int* num2, int length2) {
	int i = 0;
	int j = 0;
	int *num = new int[length1 + length2];
	int t = 0;
	while ((i < length1) && (j < length2)) {
		if (num1[i] < num2[j]) {
			num[t++] = num1[i++];
		}
		else {
			num[t++] = num2[j++];
		}
	}
	while (i < length1) {
		num[t++] = num1[i++];
	}
	while (j < length2) {
		num[t++] = num2[j++];
	}
	for (int p = 0; p < t; p++) {
		if (p < length1) {
			num1[p] = num[p];
		}
		else {
			num2[p - length1] = num[p];
		}
	}
}
void merge_sort(int* num, int length) {
	if (length > 1) {
		int length1 = length / 2;
		int length2 = length / 2 + length % 2;
		int* num1 = &num[0];
		int* num2 = &num[length1];
		merge_sort(num1, length1);
		merge_sort(num2, length2);
		merge(num1, length1, num2, length2);
	}
}

int main() {
	int num[] = {3, 2, 4, 1, 5, 0, 9, 2, 1, 6};
	int length = 10;
	merge_sort(num, length);
	print(num, length);
	system("pause");
    return 0;
}
