
public class Main {

	public static void main(String[] args) {
		A a1 = new A();
		A a2 = new A();
		
		a1.v1 = 2;
		a1.v2 = 1;
		a2.v2 = 1;
		
		System.out.println("a2.v1: " + a2.v1);
		System.out.println(a1.v1 == a2.v1); 
		System.out.println(a1.v1 == a2.v2); 
		System.out.println(a1.v2 == a2.v1);
		System.out.println(a1.v2 == a2.v2);
		
		System.out.println("uguaglianza stati a1 a2 " + (a1.v1 == a2.v1 && a1.v2==a2.v2)); 
		System.out.println("identita' " + (a1==a2));
		
		A a3;
		
		a3 = a2;
		
		System.out.println(a1.v1 == a3.v1);
		System.out.println(a1.v2 == a3.v2);
		System.out.println(a2.v1 == a3.v1);
		System.out.println(a2.v2 == a3.v2);
		System.out.println("a3==a2 " + (a3==a2));
		
		a3.v1 = 2;
		
		System.out.println("uguaglianza stati a1 a3 " + (a1.v1 == a3.v1 && a1.v2 == a3.v2));
		System.out.println(a1==a3);

	}

}
