/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hofuniversity.iws.core;

import java.util.*;

import de.hofuniversity.iws.core.game.entities.Entity;
import de.hofuniversity.iws.core.game.entities.PhysicsEntity;
import org.jbox2d.callbacks.*;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.Contact;
import playn.core.*;

import static playn.core.PlayN.graphics;

/**
 *
 * @author Daniel Heinrich <dannynullzwo@gmail.com>
 */
public class TestWorld implements ContactListener {

    public GroupLayer staticLayerBack;
    public GroupLayer dynamicLayer;
    public GroupLayer staticLayerFront;
// size of world
    private static int width = 24;
    private static int height = 18;
    // box2d object containing physics world
    public final World world;
    private List<Entity> entities = new ArrayList<Entity>(0);
    private HashMap<Body, PhysicsEntity> bodyEntityLUT = new HashMap<Body, PhysicsEntity>();
    private Stack<Contact> contacts = new Stack<Contact>();
    private static boolean showDebugDraw = false;
    private DebugDrawBox2D debugDraw;

    /**
     *
     * @param scaledLayer
     */
    public TestWorld(GroupLayer scaledLayer) {
        staticLayerBack = graphics().createGroupLayer();
        scaledLayer.add(staticLayerBack);
        dynamicLayer = graphics().createGroupLayer();
        scaledLayer.add(dynamicLayer);
        staticLayerFront = graphics().createGroupLayer();
        scaledLayer.add(staticLayerFront);

        // create the physics world
        Vec2 gravity = new Vec2(0.0f, 10.0f);
        world = new World(gravity, true);
        world.setWarmStarting(true);
        world.setAutoClearForces(true);
        world.setContactListener(this);

        // create the ground
        Body ground = world.createBody(new BodyDef());
        PolygonShape groundShape = new PolygonShape();
        groundShape.setAsEdge(new Vec2(0, height), new Vec2(width, height));
        ground.createFixture(groundShape, 0.0f);

        // create the walls
        Body wallLeft = world.createBody(new BodyDef());
        PolygonShape wallLeftShape = new PolygonShape();
        wallLeftShape.setAsEdge(new Vec2(0, 0), new Vec2(0, height));
        wallLeft.createFixture(wallLeftShape, 0.0f);
        Body wallRight = world.createBody(new BodyDef());
        PolygonShape wallRightShape = new PolygonShape();
        wallRightShape.setAsEdge(new Vec2(width, 0), new Vec2(width, height));
        wallRight.createFixture(wallRightShape, 0.0f);

        if (showDebugDraw) {
            CanvasImage image = graphics().createImage((int) (width / WebApp.physUnitPerScreenUnit),
                                                       (int) (height / WebApp.physUnitPerScreenUnit));
            graphics().rootLayer().add(graphics().createImageLayer(image));
            debugDraw = new DebugDrawBox2D();
            debugDraw.setCanvas(image);
            debugDraw.setFlipY(false);
            debugDraw.setStrokeAlpha(150);
            debugDraw.setFillAlpha(75);
            debugDraw.setStrokeWidth(2.0f);
            debugDraw.setFlags(DebugDraw.e_shapeBit | DebugDraw.e_jointBit | DebugDraw.e_aabbBit);
            debugDraw.setCamera(0, 0, 1f / WebApp.physUnitPerScreenUnit);
            world.setDebugDraw(debugDraw);
        }
    }

    public void update(float delta) {
        for (Entity e : entities) {
            e.update(delta);
        }
        // the step delta is fixed so box2d isn't affected by framerate
        world.step(0.033f, 10, 10);
        processContacts();
    }

    public void paint(float delta) {
        if (showDebugDraw) {
            debugDraw.getCanvas().clear();
            world.drawDebugData();
        }
        for (Entity e : entities) {
            e.paint(delta);
        }
    }

    // handle contacts out of physics loop
    public void processContacts() {
        while (!contacts.isEmpty()) {
            Contact contact = contacts.pop();

            // handle collision
            PhysicsEntity entityA = bodyEntityLUT.get(contact.m_fixtureA.m_body);
            PhysicsEntity entityB = bodyEntityLUT.get(contact.m_fixtureB.m_body);

            if (entityA != null && entityB != null) {
                if (entityA instanceof PhysicsEntity.HasContactListener) {
                    ((PhysicsEntity.HasContactListener) entityA).contact(entityB);
                }
                if (entityB instanceof PhysicsEntity.HasContactListener) {
                    ((PhysicsEntity.HasContactListener) entityB).contact(entityA);
                }
            }
        }
    }

    public void add(Entity entity) {
        entities.add(entity);
        if (entity instanceof PhysicsEntity) {
            PhysicsEntity physicsEntity = (PhysicsEntity) entity;
            bodyEntityLUT.put(physicsEntity.getBody(), physicsEntity);
        }
    }

    // Box2d's begin contact
    @Override
    public void beginContact(Contact contact) {
        contacts.push(contact);
    }

    // Box2d's end contact
    @Override
    public void endContact(Contact contact) {
    }

    // Box2d's pre solve
    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    // Box2d's post solve
    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }
}