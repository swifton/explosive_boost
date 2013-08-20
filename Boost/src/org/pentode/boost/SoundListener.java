package org.pentode.boost;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class SoundListener {
	ContactListener contactListener;

	public SoundListener(final Sound ball) {
		contactListener = new ContactListener() {
            public void beginContact(Contact contact) {
         	   if (contact.getFixtureB().getBody().getUserData() == "ball") ball.play();
         	   if (contact.getFixtureB().getBody().getUserData() == "brick") {} //sounds.brickSound.play();
            }
            
            public void endContact(Contact contact) {}
            public void postSolve(Contact contact, ContactImpulse impulse) {}
            public void preSolve(Contact contact, Manifold oldManifold) {}
		   };
	}
}
