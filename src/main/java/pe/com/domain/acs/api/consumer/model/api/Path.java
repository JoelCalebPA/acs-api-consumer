package pe.com.domain.acs.api.consumer.model.api;

public class Path {
    
    private String name;

    public Path() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Path [name=" + name + "]";
    }

}