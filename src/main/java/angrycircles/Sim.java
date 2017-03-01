
package angrycircles;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

import java.util.Timer;
import java.util.TimerTask;

import static angrycircles.MainApp.WIDTH;
import static angrycircles.MainApp.HEIGHT;

public class Sim {

    /**
     * The ball we're showing
     */
    Body ball, ground, floor, redBox, greenBox, blueBox;
    float ballRadius = 0.3f;
    float x ;


    /**
     * The world the ball inhabits
     */
    static World world;
    protected static final float METERS_TO_PIXELS = 10f;

    /**
     * Set up the simulation
     */
    public Sim() {
        Vec2 gravity = new Vec2(0f, -10f);
        world = new World(gravity);

        BodyDef ballDef = new BodyDef();
        ballDef.type = BodyType.DYNAMIC;
        // ballDef.position.set(WIDTH/METERS_TO_PIXELS/5, -(float)JFXApp.rec.getY()/METERS_TO_PIXELS );
        FixtureDef ballFixDef = new FixtureDef();
        ballFixDef.density = 0.9f;
        ballFixDef.shape = new CircleShape();
        ballFixDef.shape.m_radius = 10*0.1f;
        ballFixDef.friction = 0.3f;
        ballFixDef.restitution = 0.8f;
        this.ball = world.createBody(ballDef);
        ball.createFixture(ballFixDef);



        BodyDef blueBoxDef = new BodyDef();
        blueBoxDef.type = BodyType.DYNAMIC;
        blueBoxDef.position.set(
                toPosX((float) (WIDTH - (60 * 3)),
                        (float) 60),
                - toPosY( (float) (((HEIGHT*2)/3) - (60*2)),
                        (float)60));
        PolygonShape blueBoxShape = new PolygonShape();
        blueBoxShape.setAsBox((float) 60 / METERS_TO_PIXELS / 2, (float) 60 / METERS_TO_PIXELS / 2);
        FixtureDef blueBoxFixDef = new FixtureDef();
        blueBoxFixDef.density = 0.03f;
        blueBoxFixDef.shape = blueBoxShape;
        blueBoxFixDef.restitution = 0.3f;
        this.blueBox = world.createBody(blueBoxDef);
        blueBox.createFixture(blueBoxFixDef);

        BodyDef greenBoxDef = new BodyDef();
        greenBoxDef.type = BodyType.DYNAMIC;
        greenBoxDef.position.set(
                toPosX((float) (600 - (60 * 1) +  30/2),
                        (float) 30),
                - toPosY((float)(((400*2)/3)-60-30),
                        (float)30));
        PolygonShape greenBoxShape = new PolygonShape();
        greenBoxShape.setAsBox((float) 30 / METERS_TO_PIXELS / 2, (float) 30 / METERS_TO_PIXELS / 2);
        FixtureDef greenBoxFixDef = new FixtureDef();
        greenBoxFixDef.density = 0.08f;
        greenBoxFixDef.shape = greenBoxShape;
        greenBoxFixDef.restitution = 0.8f;
        this.greenBox = world.createBody(greenBoxDef);
        greenBox.createFixture(greenBoxFixDef);

        BodyDef redBoxDef = new BodyDef();
        redBoxDef.type = BodyType.DYNAMIC;
        redBoxDef.position.set(
                toPosX((float) (WIDTH - (60 * 3)),
                        (float) 60),
                - toPosY((float)(((HEIGHT*2)/3)-(60*2)-
                        60),
                        (float)60));
        PolygonShape redBoxShape = new PolygonShape();
        redBoxShape.setAsBox((float) 60 / METERS_TO_PIXELS / 2, (float) 60 / METERS_TO_PIXELS / 2);
        FixtureDef redBoxFixDef = new FixtureDef();
        redBoxFixDef.density = 0.03f;
        redBoxFixDef.shape = redBoxShape;
        redBoxFixDef.restitution = 0.8f;
        this.redBox = world.createBody(redBoxDef);
        redBox.createFixture(redBoxFixDef);


        ball.setTransform(new Vec2(12, -26)        , 0);
        ball.applyLinearImpulse(new Vec2(20,60), ball.getWorldCenter());
       // ball.setLinearVelocity(new Vec2(3, 0));



        BodyDef groundDef = new BodyDef();
        groundDef.type = BodyType.KINEMATIC;
        groundDef.position.set(
                (float) WIDTH / METERS_TO_PIXELS / 2,
                - toPosY((float)((HEIGHT*2)/3),
                        (float)50));
        FixtureDef groundFixDef = new FixtureDef();
        groundFixDef.density = 1f;
        PolygonShape blockShape = new PolygonShape();
        blockShape.setAsBox((float)480/METERS_TO_PIXELS/2, (float)50/METERS_TO_PIXELS/2);
        groundFixDef.shape = blockShape;
        groundFixDef.friction = 0.3f;
        //groundFixDef.restitution = 0.8f;
        this.ground = world.createBody(groundDef);
        ground.createFixture(groundFixDef);
        /*
        BodyDef floorDef = new BodyDef();
        floorDef.type = BodyType.KINEMATIC;
        floorDef.position.set(WIDTH / METERS_TO_PIXELS, -HEIGHT / METERS_TO_PIXELS - 1);
        FixtureDef floorFixDef = new FixtureDef();
        groundFixDef.density = 1f;
        PolygonShape floorShape = new PolygonShape();
        floorShape.setAsBox(1000f, 0f);
        floorFixDef.shape = floorShape;
        floorFixDef.friction = 0.3f;
        //groundFixDef.restitution = 0.8f;
        this.floor = world.createBody(floorDef);
        floor.createFixture(floorFixDef);
*/
        System.out.println(world.getBodyCount());
    }

    /**
     * Set the simulation running
     */
    public void start(Runnable updateUi) {
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                world.step(1 / 60f, 6, 3);
                updateUi.run();
            }
        }, 1000/60, 1000/60);
    }



    //This method adds a ground to the screen.
    public static void addGround(float width, float height){
        PolygonShape ps = new PolygonShape();
        ps.setAsBox(width,height);
        FixtureDef fd = new FixtureDef();

        fd.density = 1f;
        fd.shape = ps;
        BodyDef bd = new BodyDef();
        bd.type = BodyType.KINEMATIC;
        bd.position= new Vec2(0.0f, -(float)MainApp.root.getHeight()/METERS_TO_PIXELS);
        world.createBody(bd).createFixture(fd);
    }

    //This method creates a walls.
    public void addWall(float posX, float posY, float width, float height){
        PolygonShape ps = new PolygonShape();
        ps.setAsBox(width,height);
        FixtureDef fd = new FixtureDef();
        fd.shape = ps;
        fd.density = 1.0f;
        fd.friction = 0.3f;
        BodyDef bd = new BodyDef();
        bd.type = BodyType.KINEMATIC;
        bd.position.set(posX, posY);
        world.createBody(bd).createFixture(fd);
    }

    //Convert a JBox2D x coordinate to a JavaFX pixel x coordinate
    //UIBox.xPosition = (PhysicsBox.xPosition * meters_to_pixels) - UIBox.height / 2
    public static float toPixelPosX(float posX, float height) {
        float x = (posX * METERS_TO_PIXELS) - height / 2;
        return x;
    }

    //Convert a JavaFX pixel x coordinate to a JBox2D x coordinate
    //PhysicsBox.xPosition = (UIBox.xPosition + (UIBox.HEIGHT / 2)) / METERS_TO_PIXELS
    public static float toPosX(float posX, float height) {
        float x = (posX + (height / 2)) / METERS_TO_PIXELS;
        return x;
    }

    //Convert a JBox2D y coordinate to a JavaFX pixel y coordinate
    //UIBox.yPosition = (PhysicsBox.yPosition * METERS_TO_PIXELS) - WIDTH / 2
    public static float toPixelPosY(float posY, float width) {
        float y = (posY * METERS_TO_PIXELS) - width / 2;
        return y;
    }

    //Convert a JavaFX pixel y coordinate to a JBox2D y coordinate
    //PhysicsBox.yPosition = (UIBox.yPosition + (WIDTH / 2)) / METERS_TO_PIXELS
    public static float toPosY(float posY, float width) {
        float y = (posY + (width / 2)) / METERS_TO_PIXELS;
        return y;
    }

    //Convert a JBox2D width to pixel width
    // set UIBox.height = PhysicsBox.height * 2 * METERS_TO_PIXELS
    public static float toPixelWidth(float width) {
        return (width * 2) * METERS_TO_PIXELS;
    }

    //Convert a JBox2D height to pixel height
    public static float toPixelHeight(float height) {
        return (height * 2) * METERS_TO_PIXELS;
    }


}
