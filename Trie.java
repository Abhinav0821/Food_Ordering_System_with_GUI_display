import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class TrieNode {
    Map<Character, TrieNode> children = new HashMap<>();
    List<Item> items = new ArrayList<>(); // Store items that match this prefix
    boolean isEndOfWord = false;
}

class Trie {
    private final TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    // Insert an item name into the trie
    public void insert(String name, Item item) {
        TrieNode current = root;
        for (char ch : name.toCharArray()) {
            current.children.putIfAbsent(ch, new TrieNode());
            current = current.children.get(ch);
            current.items.add(item); // Add item to each prefix level
        }
        current.isEndOfWord = true;
    }

    // Search for items by prefix
    public List<Item> searchByPrefix(String prefix) {
        TrieNode current = root;
        for (char ch : prefix.toCharArray()) {
            if (!current.children.containsKey(ch)) {
                return new ArrayList<>(); // No items match the prefix
            }
            current = current.children.get(ch);
        }
        return new ArrayList<>(current.items); // Return all items with this prefix
    }
}
