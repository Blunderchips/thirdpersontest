package leo.thirdpersontest;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.sun.org.apache.xpath.internal.operations.Mod;

import java.util.ArrayList;

public class ThirdPersonTest extends ApplicationAdapter {
	public Camera gameCamera;
	public Camera hudCamera;
	public ModelBatch modelBatch;
	public AssetManager assetManager;
	public ArrayList<ModelInstance> modelInstances = new ArrayList<ModelInstance>();
	public Environment environment;

	@Override
	public void create () {
		gameCamera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		gameCamera.position.set(10, 5, 0);
		gameCamera.lookAt(0, 0, 0);
		gameCamera.near = 1;
		gameCamera.far = 300;
		gameCamera.update();

		hudCamera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		hudCamera.position.set(0, 0, 0);
		hudCamera.near = 1;
		hudCamera.far = 300;
		hudCamera.update();

		modelBatch = new ModelBatch();

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		assetManager = new AssetManager();
		assetManager.load("knight.g3db", Model.class);
		assetManager.load("ground.g3db", Model.class);
		assetManager.finishLoading();

		ModelInstance character = new ModelInstance(assetManager.get("knight.g3db", Model.class));
		ModelInstance ground = new ModelInstance(assetManager.get("ground.g3db", Model.class));
		modelInstances.add(character);
		modelInstances.add(ground);

		Gdx.input.setCursorCatched(true);
	}

	@Override
	public void render () {
		Gdx.input.setCursorPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);

		if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
			Gdx.app.exit();
		}

		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			modelInstances.get(0).transform.translate(-0.1f, 0, 0);
			gameCamera.translate(-0.1f, 0, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			modelInstances.get(0).transform.translate(0.1f, 0, 0);
			gameCamera.translate(0.1f, 0, 0);
		}

		gameCamera.rotateAround(modelInstances.get(0).transform.getTranslation(new Vector3()), new Vector3(0, -1, 0), Gdx.input.getDeltaX() / 3);
		gameCamera.rotateAround(modelInstances.get(0).transform.getTranslation(new Vector3()), new Vector3(0, 0, -1), Gdx.input.getDeltaY() / 3);

		/*Vector3 camPos = camera.position;
		camera.position.set(modelInstances.get(0).transform.getTranslation(new Vector3()));
		camera.rotate(new Vector3(0, Gdx.input.getDeltaX(), 0), 1);
		camera.position.set(camPos);*/

		gameCamera.update();

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		modelBatch.begin(gameCamera);
		modelBatch.render(modelInstances, environment);
		modelBatch.end();
	}
	
	@Override
	public void dispose () {
		modelBatch.dispose();
		assetManager.dispose();
	}
}
