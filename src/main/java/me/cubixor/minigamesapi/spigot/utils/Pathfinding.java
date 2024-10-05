package me.cubixor.minigamesapi.spigot.utils;

import com.cryptomorin.xseries.reflection.XReflection;
import com.cryptomorin.xseries.reflection.minecraft.MinecraftClassHandle;
import com.cryptomorin.xseries.reflection.minecraft.MinecraftPackage;
import com.google.common.collect.Sets;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.lang.invoke.MethodHandle;
import java.util.Set;

public class Pathfinding {

    private final MinecraftClassHandle entityInsentientClass;
    private final MethodHandle craftLivingEntityGetHandle;

    private final MethodHandle pathfinderGoalSelectorGoalSetter;
    private final MethodHandle entityInsentientGoalSelectorGetter;

    private final MethodHandle pathfinderGoalSelectorAddGoal;

    private final MethodHandle goalRandomLookaroundConstructor;
    private final MethodHandle goalRandomStrollConstructor;
    private final MethodHandle goalFloatConstructor;

    private final MethodHandle entityInsentientGetNavigation;
    private final MethodHandle navigationAbstractNavigateTo;
    private final MethodHandle navigationAbstractSetTimer;

    private MethodHandle followRangeGetter;
    private MethodHandle getAttributeInstance;
    private MethodHandle setAttributeModifiable;

    private boolean changeFollowRangeNeeded;

    public Pathfinding() throws ReflectiveOperationException {
        // Entity related classes
        MinecraftClassHandle craftLivingEntityClass = XReflection.ofMinecraft()
                .inPackage(MinecraftPackage.CB, "entity")
                .named("CraftLivingEntity");
        MinecraftClassHandle entityLivingClass = XReflection.ofMinecraft()
                .inPackage(MinecraftPackage.NMS, "world.entity")
                .named("EntityLiving");
        MinecraftClassHandle entityCreatureClass = XReflection.ofMinecraft()
                .inPackage(MinecraftPackage.NMS, "world.entity")
                .named("EntityCreature");
        entityInsentientClass = XReflection.ofMinecraft()
                .inPackage(MinecraftPackage.NMS, "world.entity")
                .named("EntityInsentient");

        // CraftLivingEntity#getHandle() - cast CraftLivingEntity to EntityLiving
        craftLivingEntityGetHandle = craftLivingEntityClass
                .method()
                .named("getHandle")
                .returns(entityLivingClass)
                .reflect();


        MinecraftClassHandle aiGoalPackage = XReflection.ofMinecraft()
                .inPackage(MinecraftPackage.NMS, "world.entity.ai.goal");

        MinecraftClassHandle pathfinderGoalSelectorClass = aiGoalPackage.clone()
                .named("PathfinderGoalSelector");
        MinecraftClassHandle pathfinderGoalClass = aiGoalPackage.clone()
                .named("PathfinderGoal");
        MinecraftClassHandle navigationAbstractClass = XReflection.ofMinecraft()
                .inPackage(MinecraftPackage.NMS, "world.entity.ai.navigation")
                .named("NavigationAbstract");

        // Goal classes
        goalRandomLookaroundConstructor = aiGoalPackage.clone()
                .named("PathfinderGoalRandomLookaround")
                .constructor(entityInsentientClass)
                .reflect();
        goalRandomStrollConstructor = aiGoalPackage.clone()
                .named("PathfinderGoalRandomStroll")
                .constructor(entityCreatureClass.reflect(), double.class)
                .reflect();
        goalFloatConstructor = aiGoalPackage.clone()
                .named("PathfinderGoalFloat")
                .constructor(entityInsentientClass)
                .reflect();

        // Adding goals methods
        pathfinderGoalSelectorAddGoal = pathfinderGoalSelectorClass
                .method()
                .named("a")
                .parameters(int.class, pathfinderGoalClass.reflect())
                .returns(Void.TYPE)
                .reflect();
        entityInsentientGetNavigation = entityInsentientClass
                .method()
                .named("getNavigation", "D", "G", "N")
                .returns(navigationAbstractClass)
                .reflect();
        navigationAbstractNavigateTo = navigationAbstractClass
                .method()
                .named("a")
                .parameters(double.class, double.class, double.class, double.class)
                .returns(boolean.class)
                .reflect();
        navigationAbstractSetTimer = navigationAbstractClass
                .field()
                .named(getNavigationAbstractTimerFieldName())
                .makeAccessible()
                .setter()
                .returns(long.class)
                .reflect();

        // Clearing goals methods
        pathfinderGoalSelectorGoalSetter = pathfinderGoalSelectorClass
                .field()
                .named("b", "c", "d")
                .makeAccessible()
                .setter()
                .returns(Set.class)
                .reflect();
        entityInsentientGoalSelectorGetter = entityInsentientClass
                .field()
                .named("bW", "b0", "bN", "bR", "bP", "goalSelector")
                .getter()
                .returns(pathfinderGoalSelectorClass)
                .reflect();


        // Setting follow range
        //TODO Check on which versions it's needed
        if (!XReflection.supports(20)) {
            changeFollowRangeNeeded = true;

            MinecraftClassHandle attributesPackage = XReflection.ofMinecraft()
                    .inPackage(MinecraftPackage.NMS, "world.entity.ai.attributes");

            MinecraftClassHandle genericAttributesClass = attributesPackage.clone()
                    .named("GenericAttributes");
            MinecraftClassHandle attributeBaseClass = attributesPackage.clone()
                    .named("IAttribute", "AttributeBase");
            MinecraftClassHandle attributeModifiableClass = attributesPackage.clone()
                    .named("AttributeModifiable");
            MinecraftClassHandle attributeInstanceClass = attributesPackage.clone()
                    .named("AttributeInstance");

            followRangeGetter = genericAttributesClass
                    .field()
                    .named("FOLLOW_RANGE", "m")
                    .returns(attributeBaseClass)
                    .getter()
                    .asStatic()
                    .reflect();
            getAttributeInstance = entityLivingClass
                    .method()
                    .named("getAttributeInstance")
                    .returns(attributeInstanceClass)
                    .parameters(attributeBaseClass)
                    .reflect();
            setAttributeModifiable = attributeModifiableClass
                    .method()
                    .named("setValue", "a")
                    .returns(Void.TYPE)
                    .parameters(double.class)
                    .reflect();
        }
    }

    private String getNavigationAbstractTimerFieldName() {
        return XReflection
                .v(13, "i")
                .v(12, "m")
                .v(10, "k")
                .orElse("l");
    }

    public Object getEntityInsentient(Entity entity) {
        try {
            //Object craftLivingEntity = craftLivingEntityClass.reflect().cast(entity);
            Object entityLiving = craftLivingEntityGetHandle.invoke(entity);
            return entityInsentientClass.reflect().cast(entityLiving);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }

    public void clearGoals(Object entityInsentient) {
        try {
            Object pathfinderGoalSelector = entityInsentientGoalSelectorGetter.invoke(entityInsentient);
            pathfinderGoalSelectorGoalSetter.invoke(pathfinderGoalSelector, Sets.newLinkedHashSet());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void addOtherGoals(Object entityInsentient) {
        try {
            Object pathfinderGoalSelector = entityInsentientGoalSelectorGetter.invoke(entityInsentient);

            Object goalRandomLookaround = goalRandomLookaroundConstructor.invoke(entityInsentient);
            Object goalRandomStroll = goalRandomStrollConstructor.invoke(entityInsentient, 1);
            Object goalFloat = goalFloatConstructor.invoke(entityInsentient);

            pathfinderGoalSelectorAddGoal.invoke(pathfinderGoalSelector, 0, goalFloat);
            pathfinderGoalSelectorAddGoal.invoke(pathfinderGoalSelector, 1, goalRandomStroll);
            pathfinderGoalSelectorAddGoal.invoke(pathfinderGoalSelector, 2, goalRandomLookaround);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


    public void addWalkToLocationGoal(Object entityInsentient, Location loc, double speed) {
        try {
            Object navigation = entityInsentientGetNavigation.invoke(entityInsentient);
            navigationAbstractNavigateTo.invoke(navigation, loc.getX(), loc.getY(), loc.getZ(), speed);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void changeFollowRange(Object entityInsentient, double range) {
        try {
            if (!changeFollowRangeNeeded) return;

            Object followRange = followRangeGetter.invoke();
            Object attributeInstance = getAttributeInstance.invoke(entityInsentient, followRange);
            setAttributeModifiable.invoke(attributeInstance, range);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void resetTimer(Object entityInsentient) {
        try {
            Object navigationAbstract = entityInsentientGetNavigation.invoke(entityInsentient);
            navigationAbstractSetTimer.invoke(navigationAbstract, 0);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
