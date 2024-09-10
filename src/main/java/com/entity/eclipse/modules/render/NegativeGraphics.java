package com.entity.eclipse.modules.render;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.events.render.RenderEvent;
import net.minecraft.client.option.GraphicsMode;
import net.minecraft.client.option.ParticlesMode;

public class NegativeGraphics extends Module {
    private boolean originalAO;
    private boolean originalVB;
    private int originalBB;
    private boolean originalES;
    private int originalMM;
    private GraphicsMode originalGM;
    private ParticlesMode originalPM;

    public NegativeGraphics() {
        super("NegativeGraphics", "the worst rendering", ModuleType.RENDER);
    }

    public boolean hasShading(boolean original) {
        return false;
    }

    @Override
    public void tick() {

    }

    @Override
    public void onEnable() {
        this.originalAO = Eclipse.client.options.getAo().getValue();
        this.originalBB = Eclipse.client.options.getBiomeBlendRadius().getValue();
        this.originalVB = Eclipse.client.options.getBobView().getValue();
        this.originalES = Eclipse.client.options.getEntityShadows().getValue();
        this.originalMM = Eclipse.client.options.getMipmapLevels().getValue();
        this.originalGM = Eclipse.client.options.getGraphicsMode().getValue();
        this.originalPM = Eclipse.client.options.getParticles().getValue();

        Eclipse.client.options.getAo().setValue(false);
        Eclipse.client.options.getBiomeBlendRadius().setValue(0);
        Eclipse.client.options.getBobView().setValue(false);
        Eclipse.client.options.getEntityShadows().setValue(false);
        Eclipse.client.options.getMipmapLevels().setValue(0);
        Eclipse.client.options.getGraphicsMode().setValue(GraphicsMode.FAST);
        Eclipse.client.options.getParticles().setValue(ParticlesMode.MINIMAL);

        Eclipse.client.worldRenderer.reload();
    }

    @Override
    public void onDisable() {
        Eclipse.client.options.getAo().setValue(this.originalAO);
        Eclipse.client.options.getBiomeBlendRadius().setValue(this.originalBB);
        Eclipse.client.options.getBobView().setValue(this.originalVB);
        Eclipse.client.options.getEntityShadows().setValue(this.originalES);
        Eclipse.client.options.getMipmapLevels().setValue(this.originalMM);
        Eclipse.client.options.getGraphicsMode().setValue(this.originalGM);
        Eclipse.client.options.getParticles().setValue(this.originalPM);

        Eclipse.client.worldRenderer.reload();
    }

    @Override
    public void renderWorld(RenderEvent event) {

    }

    @Override
    public void renderScreen(RenderEvent event) {

    }
}
