package proj;

public class Vehicle {
    private String id;
    private String name;
    private int capacity;
    private int price;
    private int available;

    public Vehicle(String id, String name, int capacity, int price, int available) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.price = price;
        this.available = available;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getPrice() {
        return price;
    }

    public int getAvailable() {
        return available;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", capacity='" + capacity + '\'' +
                ", price=" + price +
                ", available=" + available +
                '}';
    }
}
