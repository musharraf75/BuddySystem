import java.util.Scanner;
import java.util.HashMap;

class BuddySystem {
    private int totalMemory;
    private HashMap<Integer, Integer> memoryBlocks;

    public BuddySystem(int totalMemory) {
        this.totalMemory = totalMemory;
        this.memoryBlocks = new HashMap<>();
        memoryBlocks.put(totalMemory, 1); // Initially, one block of the entire memory
    }

    public void allocateMemory(int size) {
        int allocBlockSize = findNearestPowerOfTwo(size);
        int blockSize = findAvailableBlock(allocBlockSize);

        if (blockSize == -1) {
            System.out.println("Insufficient memory to allocate " + size + " units.");
            return;
        }

        // Reduce the count of the found block size
        memoryBlocks.put(blockSize, memoryBlocks.get(blockSize) - 1);

        // Split blocks until we reach the required size
        while (blockSize > allocBlockSize) {
            blockSize /= 2;
            memoryBlocks.put(blockSize, memoryBlocks.getOrDefault(blockSize, 0) + 2);
        }

        System.out.println("Allocated " + size + " units of memory.");
    }

    public void deallocateMemory(int size) {
        int blockSize = findNearestPowerOfTwo(size);

        if (!memoryBlocks.containsKey(blockSize) || memoryBlocks.get(blockSize) <= 0) {
            System.out.println("No block of size " + size + " found to deallocate.");
            return;
        }

        // Add the block back to memory
        memoryBlocks.put(blockSize, memoryBlocks.get(blockSize) + 1);

        // Merge buddy blocks recursively
        mergeBuddies(blockSize);
        System.out.println("Deallocated " + size + " units of memory.");
    }

    public void showMemoryStatus() {
        System.out.println("Current memory status:");
        memoryBlocks.forEach((blockSize, count) -> {
            if (count > 0) {
                System.out.println("Block size: " + blockSize + " | Available blocks: " + count);
            }
        });
    }

    private int findNearestPowerOfTwo(int size) {
        int power = 1;
        while (power < size) {
            power *= 2;
        }
        return power;
    }

    private int findAvailableBlock(int size) {
        for (int blockSize : memoryBlocks.keySet()) {
            if (blockSize >= size && memoryBlocks.get(blockSize) > 0) {
                return blockSize;
            }
        }
        return -1;
    }

    private void mergeBuddies(int blockSize) {
        while (memoryBlocks.get(blockSize) == 2) {
            memoryBlocks.put(blockSize, 0);
            blockSize *= 2;
            memoryBlocks.put(blockSize, memoryBlocks.getOrDefault(blockSize, 0) + 1);
        }
    }
}

public class BuddySystemSimulator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter total memory size (power of 2): ");
        int totalMemory = scanner.nextInt();
        BuddySystem buddySystem = new BuddySystem(totalMemory);

        while (true) {
            System.out.println("\nChoose an action:");
            System.out.println("1. Allocate memory");
            System.out.println("2. Deallocate memory");
            System.out.println("3. Show memory status");
            System.out.println("4. Exit");
            System.out.print("Your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter memory size to allocate: ");
                    int allocSize = scanner.nextInt();
                    buddySystem.allocateMemory(allocSize);
                    break;
                case 2:
                    System.out.print("Enter memory size to deallocate: ");
                    int deallocSize = scanner.nextInt();
                    buddySystem.deallocateMemory(deallocSize);
                    break;
                case 3:
                    buddySystem.showMemoryStatus();
                    break;
                case 4:
                    System.out.println("Exiting program.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}