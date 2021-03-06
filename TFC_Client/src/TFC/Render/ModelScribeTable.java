package TFC.Render;

import TFC.*;
import net.minecraft.src.Entity;
import net.minecraft.src.ModelRenderer;

//Date: 4/11/2012 9:50:15 AM
//Template version 1.1
//Java generated by Techne
//Keep in mind that you still need to fill in some blanks
//- ZeuX

public class ModelScribeTable extends ModelBaseTFC
{
//fields
 ModelRenderer Shape1;
 ModelRenderer Shape2;

public ModelScribeTable()
{
 textureWidth = 64;
 textureHeight = 32;
 
   Shape1 = new ModelRenderer(this, 0, 17);
   Shape1.addBox(0F, 0F, 0F, 2, 13, 2);
   Shape1.setRotationPoint(-1F, 11F, -1F);
   Shape1.setTextureSize(64, 32);
   Shape1.mirror = true;
   setRotation(Shape1, 0F, 0F, 0F);
   Shape2 = new ModelRenderer(this, 3, 0);
   Shape2.addBox(0F, 0F, 0F, 13, 1, 13);
   Shape2.setRotationPoint(-6.5F, 14F, -8F);
   Shape2.setTextureSize(64, 32);
   Shape2.mirror = true;
   setRotation(Shape2, 0.418879F, 0F, 0F);
}

public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
{
 super.render(entity, f, f1, f2, f3, f4, f5);
 setRotationAngles(f, f1, f2, f3, f4, f5, entity);
 Shape1.render(f5);
 Shape2.render(f5);
}

private void setRotation(ModelRenderer model, float x, float y, float z)
{
 model.rotateAngleX = x;
 model.rotateAngleY = y;
 model.rotateAngleZ = z;
}

public void renderModel(float f5){
	Shape1.render(f5);
	Shape2.render(f5);
}


public void setRotationAngles (float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
{
	super.setRotationAngles (f, f1, f2, f3, f4, f5, entity);
}

}

