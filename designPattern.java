import java.util.*;

interface Observer {
    void update(float temperature);
}

class WeatherStation {
    private List<Observer> observers = new ArrayList<>();
    private float temperature;

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void setTemperature(float temp) {
        this.temperature = temp;
        notifyObservers();
    }

    private void notifyObservers() {
        for (Observer obs : observers) {
            obs.update(temperature);
        }
    }
}

class PhoneDisplay implements Observer {
    public void update(float temperature) {}
}

class TVDisplay implements Observer {
    public void update(float temperature) {}
}

interface PaymentStrategy {
    void pay(int amount);
}

class CreditCardPayment implements PaymentStrategy {
    public void pay(int amount) {}
}

class PayPalPayment implements PaymentStrategy {
    public void pay(int amount) {}
}

class ShoppingCart {
    private PaymentStrategy paymentStrategy;

    public void setPaymentStrategy(PaymentStrategy strategy) {
        this.paymentStrategy = strategy;
    }

    public void checkout(int amount) {
        if (paymentStrategy != null) {
            paymentStrategy.pay(amount);
        }
    }
}

abstract class Document {
    abstract void open();
}

class PDFDocument extends Document {
    void open() {}
}

class WordDocument extends Document {
    void open() {}
}

abstract class DocumentFactory {
    abstract Document createDocument();

    public void openDocument() {
        Document doc = createDocument();
        doc.open();
    }
}

class PDFFactory extends DocumentFactory {
    Document createDocument() {
        return new PDFDocument();
    }
}

class WordFactory extends DocumentFactory {
    Document createDocument() {
        return new WordDocument();
    }
}

class Logger {
    private static Logger instance;

    private Logger() {}

    public static synchronized Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public void log(String message) {}
}

interface ModernPaymentGateway {
    void processPayment(double amount);
}

class LegacyPaymentSystem {
    public void makeLegacyPayment(String currency, double amt) {}
}

class LegacyPaymentAdapter implements ModernPaymentGateway {
    private LegacyPaymentSystem legacySystem;

    public LegacyPaymentAdapter(LegacyPaymentSystem legacy) {
        this.legacySystem = legacy;
    }

    public void processPayment(double amount) {
        legacySystem.makeLegacyPayment("USD", amount);
    }
}

abstract class Coffee {
    String description = "Basic Coffee";

    public String getDescription() {
        return description;
    }

    public abstract double cost();
}

class SimpleCoffee extends Coffee {
    public SimpleCoffee() {
        description = "Simple Coffee";
    }

    public double cost() {
        return 2.0;
    }
}

abstract class CoffeeDecorator extends Coffee {
    protected Coffee decoratedCoffee;

    public CoffeeDecorator(Coffee coffee) {
        this.decoratedCoffee = coffee;
    }

    public String getDescription() {
        return decoratedCoffee.getDescription();
    }

    public double cost() {
        return decoratedCoffee.cost();
    }
}

class MilkDecorator extends CoffeeDecorator {
    public MilkDecorator(Coffee coffee) {
        super(coffee);
    }

    public String getDescription() {
        return decoratedCoffee.getDescription() + ", Milk";
    }

    public double cost() {
        return decoratedCoffee.cost() + 0.5;
    }
}

class SugarDecorator extends CoffeeDecorator {
    public SugarDecorator(Coffee coffee) {
        super(coffee);
    }

    public String getDescription() {
        return decoratedCoffee.getDescription() + ", Sugar";
    }

    public double cost() {
        return decoratedCoffee.cost() + 0.2;
    }
}

public class DesignPatternsDemo {
    public static void main(String[] args) {}
}
