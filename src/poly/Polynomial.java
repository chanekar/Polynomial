package poly;

import java.io.IOException;
import java.util.Scanner;

/**
 * This class implements evaluate, add and multiply for polynomials.
 * 
 * @author runb-cs112
 *
 */
public class Polynomial {
	
	/**
	 * Reads a polynomial from an input stream (file or keyboard). The storage format
	 * of the polynomial is:
	 * <pre>
	 *     <coeff> <degree>
	 *     <coeff> <degree>
	 *     ...
	 *     <coeff> <degree>
	 * </pre>
	 * with the guarantee that degrees will be in descending order. For example:
	 * <pre>
	 *      4 5
	 *     -2 3
	 *      2 1
	 *      3 0
	 * </pre>
	 * which represents the polynomial:
	 * <pre>
	 *      4*x^5 - 2*x^3 + 2*x + 3 
	 * </pre>
	 * 
	 * @param sc Scanner from which a polynomial is to be read
	 * @throws IOException If there is any input error in reading the polynomial
	 * @return The polynomial linked list (front node) constructed from coefficients and
	 *         degrees read from scanner
	 */
	public static Node read(Scanner sc) 
	throws IOException {
		Node poly = null;
		while (sc.hasNextLine()) {
			Scanner scLine = new Scanner(sc.nextLine());
			poly = new Node(scLine.nextFloat(), scLine.nextInt(), poly);
			scLine.close();
		}
		return poly;
	}
	
	/**
	 * Returns the sum of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list
	 * @return A new polynomial which is the sum of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node add(Node poly1, Node poly2) {

		Node ptr1 = poly1;
		Node ptr2 = poly2;
		Node head = null;
		float c = 0;
		int d = 0;
		
		while (ptr1 != null && ptr2 != null) {
			
			if (ptr1.term.degree == ptr2.term.degree) {
				c = ptr1.term.coeff + ptr2.term.coeff;
				d = ptr1.term.degree;
				ptr1 = ptr1.next;	
				ptr2 = ptr2.next;
			} else {
				if (ptr1.term.degree < ptr2.term.degree) {
					
					c = ptr1.term.coeff;
					d = ptr1.term.degree;
					
					if (ptr1 != null) ptr1 = ptr1.next;
					
				} else {
					
					c = ptr2.term.coeff;
					d = ptr2.term.degree;
					
					if (ptr2 != null) ptr2 = ptr2.next;
				}
			}
	
			if (c != 0) head = new Node (c, d, head);
					
		}
		
		while (ptr1 != null) {
			if (ptr2 == null) {
				head = new Node (ptr1.term.coeff, ptr1.term.degree, head);
				ptr1 = ptr1.next;
			}
		}
		
		while (ptr2 != null) {
			if (ptr1 == null) {
				head = new Node (ptr2.term.coeff, ptr2.term.degree, head);
				ptr2 = ptr2.next;
			}
		}
		
		return order (head);
		
	}
	
	private static Node order (Node front) {
		int maxdegree = 0;
		Node ptr = front;
		
		while (ptr != null) {
			if (ptr.term.degree > maxdegree) maxdegree = ptr.term.degree;
			ptr = ptr.next;
		}
		
		ptr = front;
		Node ordered = null;
		while (maxdegree >= 0) {
			while (ptr != null) {
				if (ptr.term.degree == maxdegree) {
					if (ptr.term.coeff != 0) ordered = new Node (ptr.term.coeff, ptr.term.degree, ordered);
				}
				ptr = ptr.next;
			}
			ptr = front;
			maxdegree--;
		}
		
		return ordered;
	}
	 
	/**
	 * Returns the product of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list)
	 * @return A new polynomial which is the product of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node multiply(Node poly1, Node poly2) {
		
		if (poly1 == null || poly2 == null) return null;
		
		Node ptr1 = poly1;
		Node ptr2 = poly2;
		Node head = null;
		float c = 0;
		int d = 0;
		
		while (ptr1 != null) {
			while (ptr2 != null) {
				c = ptr1.term.coeff * ptr2.term.coeff;
				d = ptr1.term.degree + ptr2.term.degree;
				
				if (c != 0) head = new Node (c, d, head);
				
				ptr2 = ptr2.next;
			}
			
			ptr2 = poly2;
			ptr1 = ptr1.next;
		}
		
		head = order (head);
		
		int maxdegree = 0;
		Node ptr = head;
		
		while (ptr != null) {
			if (ptr.term.degree > maxdegree) maxdegree = ptr.term.degree;
			ptr = ptr.next;
		}
		
		ptr = head;
		Node product = null;
		float sum = 0;
		while (maxdegree >= 0) {
			while (ptr != null) {
				if (ptr.term.degree == maxdegree) {
					sum += ptr.term.coeff;
				} 
				ptr = ptr.next;
			}
			
			if (sum!= 0) product = new Node (sum, maxdegree, product);
			ptr = head;
			maxdegree--;
			sum = 0;
		}
		
		return product; 
		
	}
		
	/**
	 * Evaluates a polynomial at a given value.
	 * 
	 * @param poly Polynomial (front of linked list) to be evaluated
	 * @param x Value at which evaluation is to be done
	 * @return Value of polynomial p at x
	 */
	public static float evaluate(Node poly, float x) {
		Node ptr = poly; 
		float eval = 0;
		double power = 1;
		while (ptr != null) {
			power = Math.pow(x, ptr.term.degree);
			eval += power * ptr.term.coeff;
			ptr = ptr.next;
		}
		return eval;
	}
	
	/**
	 * Returns string representation of a polynomial
	 * 
	 * @param poly Polynomial (front of linked list)
	 * @return String representation, in descending order of degrees
	 */
	public static String toString(Node poly) {
		if (poly == null) {
			return "0";
		} 
		
		String retval = poly.term.toString();
		for (Node current = poly.next ; current != null ;
		current = current.next) {
			retval = current.term.toString() + " + " + retval;
		}
		return retval;
	}	
}
