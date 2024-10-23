package caju;
import java.util.Stack;

public class SymbolTableManager {

    private Stack<CustomHashMap> tableStack;

    public SymbolTableManager() {
        tableStack = new Stack<>();
        tableStack.push(new CustomHashMap());
    }

    public void enterScope() {
        tableStack.push(new CustomHashMap());
    }

    public void exitScope() {
        if (tableStack.size() > 1) {
            tableStack.pop();
        } else {
            throw new IllegalStateException("Não é possível sair do escopo global.");
        }
    }

    public void addSymbol(String name, Symbol symbol) {
        tableStack.peek().put(name, symbol);
    }

    public Symbol lookup(String name) {
        for (int i = tableStack.size() - 1; i >= 0; i--) {
            if (tableStack.get(i).containsKey(name)) {
                return tableStack.get(i).get(name);
            }
        }
        return null;
    }

    private class CustomHashMap {

        private static final int SIZE = 128;
        private static final int SHIFT = 4;

        private Entry[] table = new Entry[SIZE];

        private int hash(String key) {
            int temp = 0;
            int i = 0;
            while (i < key.length()) {
                temp = ((temp << SHIFT) + key.charAt(i)) % SIZE;
                i++;
            }
            return temp;
        }

        public void put(String key, Symbol value) {
            int hash = hash(key);
            if (table[hash] == null) {
                table[hash] = new Entry(key, value);
            } else {
                Entry current = table[hash];
                while (current.next != null && !current.key.equals(key)) {
                    current = current.next;
                }
                if (current.key.equals(key)) {
                    current.value = value;
                } else {
                    current.next = new Entry(key, value);
                }
            }
        }

        public Symbol get(String key) {
            int hash = hash(key);
            Entry current = table[hash];
            while (current != null && !current.key.equals(key)) {
                current = current.next;
            }
            return (current != null) ? current.value : null;
        }

        public boolean containsKey(String key) {
            return get(key) != null;
        }

        private class Entry {
            String key;
            Symbol value;
            Entry next;

            Entry(String key, Symbol value) {
                this.key = key;
                this.value = value;
                this.next = null;
            }
        }
    }

    public static class Symbol {
        private String type;
        private Object value;

        public Symbol(String type, Object value) {
            this.type = type;
            this.value = value;
        }

        public String getType() {
            return type;
        }

        public Object getValue() {
            return value;
        }
        
        public void setValue(Object value) {
            this.value = value;
        }
    }

}
