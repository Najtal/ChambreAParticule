package pakman;

import java.awt.*;

/**
 * Created by jvdur on 09/02/2016.
 */
public class StartPosition {
    public int posx;
    public int posy;
    public Color color;

    public StartPosition() {
    }

    public StartPosition(int x, int y) {
        this.posx = x;
        this.posy = y;
    }

    public StartPosition(int x, int y, Color color) {
        this.posx = x;
        this.posy = y;
        this.color = color;
    }

}
