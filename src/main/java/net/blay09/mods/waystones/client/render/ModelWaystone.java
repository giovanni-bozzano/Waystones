package net.blay09.mods.waystones.client.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelWaystone extends ModelBase
{
    private final ModelRenderer top;
    private final ModelRenderer topMidTop;
    private final ModelRenderer pillar;
    private final ModelRenderer topBottom;
    private final ModelRenderer baseTop;
    private final ModelRenderer topMidBottom;
    private final ModelRenderer baseMid;
    private final ModelRenderer baseBottom;

    public ModelWaystone()
    {
        this.textureWidth = 256;
        this.textureHeight = 256;

        this.top = new ModelRenderer(this, 0, 0);
        this.top.addBox(-8f, -64f, -8f, 16, 4, 16);

        this.topMidTop = new ModelRenderer(this, 64, 0);
        this.topMidTop.addBox(-10f, -60f, -10f, 20, 4, 20);

        this.topMidBottom = new ModelRenderer(this, 0, 76);
        this.topMidBottom.addBox(-14f, -56f, -14f, 28, 4, 28);

        this.topBottom = new ModelRenderer(this, 0, 24);
        this.topBottom.addBox(-12f, -52f, -12f, 24, 4, 24);

        this.pillar = new ModelRenderer(this, 144, 0);
        this.pillar.addBox(-10f, -48f, -10f, 20, 28, 20);

        this.baseTop = new ModelRenderer(this, 96, 48);
        this.baseTop.addBox(-12f, -20f, -12f, 24, 4, 24);

        this.baseMid = new ModelRenderer(this, 112, 76);
        this.baseMid.addBox(-14f, -16f, -14f, 28, 8, 28);

        this.baseBottom = new ModelRenderer(this, 0, 112);
        this.baseBottom.addBox(-16f, -8f, -16f, 32, 8, 32);
    }

    public void renderAll()
    {
        float f = 0.0625f;
        this.top.render(f);
        this.topMidTop.render(f);
        this.topMidBottom.render(f);
        this.topBottom.render(f);
        this.pillar.render(f);
        this.baseTop.render(f);
        this.baseMid.render(f);
        this.baseBottom.render(f);
    }

    public void renderPillar()
    {
        float f = 0.0625f;
        this.pillar.render(f);
    }
}
