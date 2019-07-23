public class Cat {
    public String name;
    public int age;
    public String color;

    public Cat(String name, int age, String color) {
        this.name = name;
        this.age = age;
        this.color = color;
    }

    public Cat() {
        this.name = "Paul";
        this.age = 2;
        this.color = "white";
    }

    @Test(prioritet = 4)
    public void meow() {
        System.out.println("meow...");
    }

    @AfterSuite
    public void jump() {
        System.out.println("jump...");
    }

    @BeforeSuite
    public void getName() {
        System.out.println("name: " + name);
    }

    @Test(prioritet = 5)
    public void getAge() {
        System.out.println("age: " + age);
    }

    @Test(prioritet = 2)
    public void getColor() {
        System.out.println("color: " + color);
    }

    @Test
    public void purr() {
        System.out.println("purr...");
    }

    public void speakTheHumanLanguage(String words) {
        System.out.println(words);
    }
}