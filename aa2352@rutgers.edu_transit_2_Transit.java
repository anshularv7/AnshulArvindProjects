package transit;

import java.util.ArrayList;

/**
 * This class contains methods which perform various operations on a layered linked
 * list to simulate transit
 * 
 * @author Ishaan Ivaturi
 * @author Prince Rawal
 */
public class Transit {
	private TNode trainZero; // a reference to the zero node in the train layer

	/* 
	 * Default constructor used by the driver and Autolab. 
	 * DO NOT use in your code.
	 * DO NOT remove from this file
	 */ 
	public Transit() { trainZero = null; }

	/* 
	 * Default constructor used by the driver and Autolab. 
	 * DO NOT use in your code.
	 * DO NOT remove from this file
	 */
	public Transit(TNode tz) { trainZero = tz; }
	
	/*
	 * Getter method for trainZero
	 *
	 * DO NOT remove from this file.
	 */
	public TNode getTrainZero () {
		return trainZero;
	}

	/**
	 * Makes a layered linked list representing the given arrays of train stations, bus
	 * stops, and walking locations. Each layer begins with a location of 0, even though
	 * the arrays don't contain the value 0. Store the zero node in the train layer in
	 * the instance variable trainZero.
	 * 
	 * @param trainStations Int array listing all the train stations
	 * @param busStops Int array listing all the bus stops
	 * @param locations Int array listing all the walking locations (always increments by 1)
	 */
	public void makeList(int[] trainStations, int[] busStops, int[] locations) {
		trainZero = new TNode(0);
		TNode busZero = new TNode(0);
		trainZero.setDown(busZero);
		TNode walkingZero = new TNode(0);
		busZero.setDown(walkingZero);
		TNode current = walkingZero;
		for (int i = 0; i < locations.length; i++) {
			current.setNext(new TNode(locations[i]));
			current = current.getNext();
		}
		current = busZero;
		for (int i = 0; i < busStops.length; i++) {
			current.setNext(new TNode(busStops[i]));
			current = current.getNext();
			TNode currentDown = walkingZero;
			while (current != null && currentDown != null) {
				if (current.getLocation() == currentDown.getLocation())
					current.setDown(currentDown);
				currentDown = currentDown.getNext();
			}
		}
		current = trainZero;
		for (int i = 0; i < trainStations.length; i++) {
			current.setNext(new TNode(trainStations[i]));
			current = current.getNext();
			TNode currentDown = busZero;
			while (current != null && currentDown != null) {
				if (current.getLocation() == currentDown.getLocation())
					current.setDown(currentDown);
				currentDown = currentDown.getNext();
			}
		}
	}
	
	/**
	 * Modifies the layered list to remove the given train station but NOT its associated
	 * bus stop or walking location. Do nothing if the train station doesn't exist
	 * 
	 * @param station The location of the train station to remove
	 */
	public void removeTrainStation(int station) {
		if (station <= 0)
			return;
		TNode current = trainZero;
		while (current != null && current.getNext() != null) {
			if (current.getNext().getLocation() == station)
				current.setNext(current.getNext().getNext());
			current = current.getNext();
		}
	}

	/**
	 * Modifies the layered list to add a new bus stop at the specified location. Do nothing
	 * if there is no corresponding walking location.
	 * 
	 * @param busStop The location of the bus stop to add
	 */
	public void addBusStop(int busStop) {
		if (busStop <= 0)
			return;
		TNode current = trainZero.getDown();
		TNode currentDown = current.getDown();
		while (currentDown != null && currentDown.getLocation() != busStop)
			currentDown = currentDown.getNext();
		while (current != null && current.getNext() != null) {
			if (current.getLocation() < busStop && current.getNext().getLocation() > busStop) {
				current.setNext(new TNode(busStop, current.getNext(), currentDown));
				return;
			}
			current = current.getNext();
		}
	}
	
	/**
	 * Determines the optimal path to get to a given destination in the walking layer, and 
	 * collects all the nodes which are visited in this path into an arraylist. 
	 * 
	 * @param destination An int representing the destination
	 * @return
	 */
	public ArrayList<TNode> bestPath(int destination) {
		ArrayList<TNode> path = new ArrayList<TNode>();
		TNode current = trainZero;
		path.add(current);
		while (current != null && current.getNext() != null && current.getNext().getLocation() <= destination) {
			current = current.getNext();
			path.add(current);
		}
		current = current.getDown();
		path.add(current);
		while (current != null && current.getNext() != null && current.getNext().getLocation() <= destination) {
			current = current.getNext();
			path.add(current);
		}
		current = current.getDown();
		path.add(current);
		while (current != null && current.getNext() != null && current.getNext().getLocation() <= destination) {
			current = current.getNext();
			path.add(current);
		}
		return path;
	}

	/**
	 * Returns a deep copy of the given layered list, which contains exactly the same
	 * locations and connections, but every node is a NEW node.
	 * 
	 * @return A reference to the train zero node of a deep copy
	 */
	public TNode duplicate() {
	    int trainStationsCount = 0;
	    TNode current = trainZero;
	    while (current.getNext() != null) {
	    	trainStationsCount++;
	    	current = current.getNext();
	    }
	    int[] trainStations = new int[trainStationsCount];
	    current = trainZero.getNext();
	    for (int i = 0; i < trainStationsCount; i++) {
	    	trainStations[i] = current.getLocation();
	    	current = current.getNext();
	    }
	    int busStopsCount = 0;
	    current = trainZero.getDown();
	    while (current.getNext() != null) {
	    	busStopsCount++;
	    	current = current.getNext();
	    }
	    int[] busStops = new int[busStopsCount];
	    current = trainZero.getDown().getNext();
	    for (int i = 0; i < busStopsCount; i++) {
	    	busStops[i] = current.getLocation();
	    	current = current.getNext();
	    }
	    int locationsCount = 0;
	    current = trainZero.getDown().getDown();
	    while (current.getNext() != null) {
	    	locationsCount++;
	    	current = current.getNext();
	    }
	    int[] locations = new int[locationsCount];
	    current = trainZero.getDown().getDown().getNext();
	    for (int i = 0; i < locationsCount; i++) {
	    	locations[i] = current.getLocation();
	    	current = current.getNext();
	    }
	    Transit result = new Transit();
	    result.makeList(trainStations, busStops, locations);
	    return result.trainZero;
	}

	/**
	 * Modifies the given layered list to add a scooter layer in between the bus and
	 * walking layer.
	 * 
	 * @param scooterStops An int array representing where the scooter stops are located
	 */
	public void addScooter(int[] scooterStops) {
		TNode busZero = trainZero.getDown();
		TNode walkingZero = trainZero.getDown().getDown();
		TNode scooterZero = new TNode(0);
		scooterZero.setDown(walkingZero);
		busZero.setDown(scooterZero);
		TNode current = scooterZero;
		for (int i = 0; i < scooterStops.length; i++) {
			current.setNext(new TNode(scooterStops[i]));
			current = current.getNext();
			TNode currentDown = walkingZero;
			while (current != null && currentDown != null) {
				if (current.getLocation() == currentDown.getLocation())
					current.setDown(currentDown);
				currentDown = currentDown.getNext();
			}
		}
		current = busZero.getNext();
		while (current != null) {
			current.setDown(null);
			TNode currentDown = scooterZero;
			while (current != null && currentDown != null) {
				if (current.getLocation() == currentDown.getLocation())
					current.setDown(currentDown);
				currentDown = currentDown.getNext();
			}
			current = current.getNext();
		}
	}

	/**
	 * Used by the driver to display the layered linked list. 
	 * DO NOT edit.
	 */
	public void printList() {
		// Traverse the starts of the layers, then the layers within
		for (TNode vertPtr = trainZero; vertPtr != null; vertPtr = vertPtr.getDown()) {
			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				// Output the location, then prepare for the arrow to the next
				StdOut.print(horizPtr.getLocation());
				if (horizPtr.getNext() == null) break;
				
				// Spacing is determined by the numbers in the walking layer
				for (int i = horizPtr.getLocation()+1; i < horizPtr.getNext().getLocation(); i++) {
					StdOut.print("--");
					int numLen = String.valueOf(i).length();
					for (int j = 0; j < numLen; j++) StdOut.print("-");
				}
				StdOut.print("->");
			}

			// Prepare for vertical lines
			if (vertPtr.getDown() == null) break;
			StdOut.println();
			
			TNode downPtr = vertPtr.getDown();
			// Reset horizPtr, and output a | under each number
			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				while (downPtr.getLocation() < horizPtr.getLocation()) downPtr = downPtr.getNext();
				if (downPtr.getLocation() == horizPtr.getLocation() && horizPtr.getDown() == downPtr) StdOut.print("|");
				else StdOut.print(" ");
				int numLen = String.valueOf(horizPtr.getLocation()).length();
				for (int j = 0; j < numLen-1; j++) StdOut.print(" ");
				
				if (horizPtr.getNext() == null) break;
				
				for (int i = horizPtr.getLocation()+1; i <= horizPtr.getNext().getLocation(); i++) {
					StdOut.print("  ");

					if (i != horizPtr.getNext().getLocation()) {
						numLen = String.valueOf(i).length();
						for (int j = 0; j < numLen; j++) StdOut.print(" ");
					}
				}
			}
			StdOut.println();
		}
		StdOut.println();
	}
	
	/**
	 * Used by the driver to display best path. 
	 * DO NOT edit.
	 */
	public void printBestPath(int destination) {
		ArrayList<TNode> path = bestPath(destination);
		for (TNode vertPtr = trainZero; vertPtr != null; vertPtr = vertPtr.getDown()) {
			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				// ONLY print the number if this node is in the path, otherwise spaces
				if (path.contains(horizPtr)) StdOut.print(horizPtr.getLocation());
				else {
					int numLen = String.valueOf(horizPtr.getLocation()).length();
					for (int i = 0; i < numLen; i++) StdOut.print(" ");
				}
				if (horizPtr.getNext() == null) break;
				
				// ONLY print the edge if both ends are in the path, otherwise spaces
				String separator = (path.contains(horizPtr) && path.contains(horizPtr.getNext())) ? ">" : " ";
				for (int i = horizPtr.getLocation()+1; i < horizPtr.getNext().getLocation(); i++) {
					StdOut.print(separator + separator);
					
					int numLen = String.valueOf(i).length();
					for (int j = 0; j < numLen; j++) StdOut.print(separator);
				}

				StdOut.print(separator + separator);
			}
			
			if (vertPtr.getDown() == null) break;
			StdOut.println();

			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				// ONLY print the vertical edge if both ends are in the path, otherwise space
				StdOut.print((path.contains(horizPtr) && path.contains(horizPtr.getDown())) ? "V" : " ");
				int numLen = String.valueOf(horizPtr.getLocation()).length();
				for (int j = 0; j < numLen-1; j++) StdOut.print(" ");
				
				if (horizPtr.getNext() == null) break;
				
				for (int i = horizPtr.getLocation()+1; i <= horizPtr.getNext().getLocation(); i++) {
					StdOut.print("  ");

					if (i != horizPtr.getNext().getLocation()) {
						numLen = String.valueOf(i).length();
						for (int j = 0; j < numLen; j++) StdOut.print(" ");
					}
				}
			}
			StdOut.println();
		}
		StdOut.println();
	}
}
