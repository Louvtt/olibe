package olibe.core;

import olibe.io.ShaderLibrary;
import olibe.scene.*;

public abstract class Renderer {
  protected Pipeline pipeline;
  protected Scene scene;
  
  public Renderer(Pipeline pipeline) {
    this.pipeline = pipeline;
    this.scene    = pipeline.getScene();
  }

  protected abstract void setupScene();

  protected abstract void loop();

  public void run() {
    this.pipeline.setup();
    this.setupScene();

    while(this.pipeline.isRunning()) {
      this.pipeline.beginFrame();
      { 
        Time d = this.pipeline.getTimeData();
        this.pipeline.update();

        this.loop();

        ShaderLibrary.Get().setUniform("uTime", d.time);
        this.pipeline.render();
      }
      this.pipeline.endFrame();
    }

    this.pipeline.delete();
  }

}
