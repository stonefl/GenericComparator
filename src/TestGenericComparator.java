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
		printPersonList(personList);
		
		//sort personList by name field 
		Collections.sort(personList, new GenericComparator("name"));
		printPersonList(personList);
		//sort personList by id field in ascending order
		Collections.sort(personList, new GenericComparator(true,"id"));
		printPersonList(personList);
		//sort personList by id field in descending order
		Collections.sort(personList, new GenericComparator(false,"id"));
		printPersonList(personList);
		//sort personList by pay field's start salary
		Collections.sort(personList, new GenericComparator("pay","startSalary"));
		printPersonList(personList);
		
		//test primitive list
		List<Integer> iList = new ArrayList<Integer>();
		iList.add(12);
		iList.add(15);
		iList.add(13);
		iList.add(20);
		System.out.println(iList);
		
		//sort iList in descending order
		Collections.sort(iList, new GenericComparator(false));
		System.out.println(iList);
		
	}

	/**
	 * Print person list.
	 * @param personList
	 */
	private static void printPersonList(List<Person> personList) {
		for(Person p: personList){
			System.out.println(p.getName() + "\t" + p.getId() + "\t" + p.getStartSalary() + "\t" +p.getStartBonus());
		}
		System.out.println("-------------");
	}

}
