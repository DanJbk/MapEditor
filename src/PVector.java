public class PVector {
    private double x;
    private double y;

    PVector(double x, double y){
        this.x = x;
        this.y = y;
    }

    PVector(PVector vactor){
        this(vactor.getX(),vactor.getY());
    }

    public void set(PVector vector){
        setX(vector.getX());
        setY(vector.getY());
    }

    public void set(double x, double y){
        setX(x);
        setY(y);
    }

    public PVector absvector(){
        return new PVector(Math.abs(getX()),Math.abs(getY()));
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getsize(){
        return Math.sqrt((x*x)+(y*y));
    }

    public PVector normalize(){
        //double size = Math.abs(x)+Math.abs(y);
        double size = getsize();
        return new PVector(x/size,y/size);
    }

    public void round(){
        setX(Math.round(getX()));
        setY(Math.round(getY()));
    }

    public PVector add(double num){return  new PVector(getX()+num,getY()+num);}

    public PVector addvector(PVector f){
        return new PVector(this.getX() + f.getX(),this.getY() + f.getY());
    }

    public PVector subvector(PVector f){
        return new PVector(this.getX() - f.getX(),this.getY() - f.getY());
    }

    public PVector mult(double num){
        return new PVector((getX()*num),(getY()*num));
    }

    public PVector multvector(PVector vec){
        return new PVector((getX()*vec.getX()),(getY()*vec.getY()));
    }

    public boolean equals(PVector vec){
        return vec.getX() == getX() && vec.getY() == getY();
    }
    @Override
    public String toString() {
        return "X[" + this.getX() + "] Y[" + this.getY() + "]";
    }
}
