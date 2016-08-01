import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public class TestGenericComparator {

	public static void main(String[] args) {
		
		// Build a list of persons with different names and ids
		List<Person> personList = new ArrayList<Person>();
		personList.add(new Person("Angela", 102, 50000, 5000));
		personList.add(new Person("Mike",101, 65000, 5500));
		personList.add(new Person("Lisa", 104, 75000,4500));
		personList.add(new Person("Bob", 103, 58000, 4300));
		//print initial person list
		System.out.println("Original List:");
		printPersonList(personList);
		
		//sort personList by name field 
		System.out.println("Sort by name field in alphabetical order:");
		Collections.sort(personList, new GenericComparator("name"));
		printPersonList(personList);
		
		//sort personList by id field in ascending order
		System.out.println("Sort by id field in ascending order:");
		Collections.sort(personList, new GenericComparator(true,"id"));
		printPersonList(personList);
		
		//sort personList by id field in descending order
		System.out.println("Sort by id field in descending order:");
		Collections.sort(personList, new GenericComparator(false,"id"));
		printPersonList(personList);
		
		
		//sort personList by pay field's start salary
		System.out.println("Sort by pay's starting salary in ascending order:");
		Collections.sort(personList, new GenericComparator("pay.startSalary"));
		printPersonList(personList);
		
	}

	/**
	 * Print person list.
	 * @param personList
	 */
	private static void printPersonList(List<Person> personList) {
		System.out.println("-------------");
		for(Person p: personList){
			System.out.println(p.getName() + "\t" + p.getId() + "\t" + p.getStartSalary() + "\t" +p.getStartBonus());
		}
		System.out.println();
	}

}
