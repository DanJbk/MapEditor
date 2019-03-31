public class DrawingCoords {
    private double GRIDSIZE;
    private PVector indexy;
    private PVector indsxy;
    private boolean fixedCoords;
    private boolean mouseDown;

    DrawingCoords(){
        GRIDSIZE = Main.GRIDSIZE;
        indexy = new PVector(0,0);;
        indsxy = new PVector(0,0);
        fixedCoords = false;
        mouseDown = false;
    }

    public void update(int gridsize, double zoom, boolean sticktogrid, boolean mousedown){
        GRIDSIZE = gridsize*zoom;
        fixedCoords = sticktogrid;
        mouseDown = mousedown;

        updateCoordinates();
    }

    public PVector[] fixcoordinates(){                                                          // coordinates
        if (!fixedCoords){return fixCoordinatesFree();}
        return fixCoordinatesGrid();
    }


    public PVector[] fixCoordinatesFree(){                                                  // swaps values if needed

        PVector sxy = new PVector(indsxy);
        PVector exy = new PVector(indexy);

        if (sxy.getX() > exy.getX()){
            double temp = sxy.getX();
            sxy.setX(exy.getX());
            exy.setX(temp);
        }
        if (sxy.getY() > exy.getY()){
            double temp = sxy.getY();
            sxy.setY(exy.getY());
            exy.setY(temp);
        }
        return new PVector[]{sxy,exy};
    }

    public PVector[] getSizeAndPlace(){                                                          // returns correct position and size of shape
        PVector[] coords = fixcoordinates();
        PVector size = coords[0].subvector(coords[1]).absvector();
        PVector place = coords[0].addvector(size.mult(0.5));

        return new PVector[]{size, place};
    }

    private PVector gridstartxy(){                                                                                                                                          // fit selection coordinates to grid
        PVector bias = new PVector(                                                                                                                                        // camera position and bias vector
                (Main.camerapose.getX()*Main.zoom)%GRIDSIZE,
                (Main.camerapose.getY()*Main.zoom)%GRIDSIZE
        );
        PVector coords = Main.mouseWinPose().subvector(bias).mult(1.0/GRIDSIZE);                                                  // subtracting the bias
        coords.set(Math.floor(coords.getX()),Math.floor(coords.getY()));                                                                           // fits to grid
        return coords.mult(GRIDSIZE).addvector(bias);                                                                                                      // return and define "jumps" in movements
    }

    public void updateCoordinates(){                                                                                                                                   // set coordinates to mouse position
        if (!mouseDown) {
            indsxy.set(fixedCoords ? gridstartxy() : Main.mouseWinPose());
        }
        indsxy.set(indsxy.addvector(Main.cameravel.mult(Main.zoom)));
        indexy.set(Main.mouseWinPose());
    }

    public PVector[] fixCoordinatesGrid(){                                                                                                                          // get correct coordinates for "sticktogrid" option

        PVector sxy = new PVector(indsxy);
        PVector exy = new PVector(indexy);

        PVector pos = gridstartxy();

        double x2 = (Math.round(pos.getX()+GRIDSIZE));
        double y2 = (Math.round(pos.getY()+GRIDSIZE));

        if (sxy.getX() > exy.getX()){
            double tempx = sxy.getX()+ GRIDSIZE;
            sxy.setX(x2 - GRIDSIZE);
            x2= tempx;
        }

        if (sxy.getY() > exy.getY()){
            double tempy = sxy.getY() + GRIDSIZE;
            sxy.setY(y2 - GRIDSIZE);
            y2= tempy;
        }

        return new PVector[]{sxy, new PVector(x2,y2)};
    }

    public PVector getIndexy() { return indexy; }
    public PVector getIndsxy() { return indsxy; }
}
