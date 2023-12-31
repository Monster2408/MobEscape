package xyz.mlserver.mobescape.utils;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import org.bukkit.Location;
import xyz.mlserver.mobescape.MobEscape;
import xyz.mlserver.mobescape.utils.game.MobEscapeMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public final class WorldEditHook {

    private static final File SCHEMATIC_DIR = new File(MobEscape.getPlugin().getDataFolder() + "/schematics/");

    public static File getSchematicFile(MobEscapeMap map) {
        return getSchematicFile(map.getId());
    }

    public static File getSchematicFile(int id) {
        return getSchematicFile(String.valueOf(id));
    }

    public static File getSchematicFile(String name) {
        if (!SCHEMATIC_DIR.exists()) SCHEMATIC_DIR.mkdirs();
        return new File(SCHEMATIC_DIR, name + ".schematic");
    }

    public static boolean loadSchematic(int id, Location to) {
        return loadSchematic(getSchematicFile(String.valueOf(id)), to);
    }

    public static boolean loadSchematic(File file, Location to) {
        if (!SCHEMATIC_DIR.exists()) SCHEMATIC_DIR.mkdirs();
        World world = new BukkitWorld(to.getWorld());
        Clipboard clipboard;
        ClipboardFormat format = ClipboardFormat.findByFile(file);
        try {
            ClipboardReader reader = format.getReader(new FileInputStream(file));
            clipboard = reader.read(world.getWorldData());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        if (clipboard == null) return false;
        EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(world, -1);
        Operation operation = new ClipboardHolder(clipboard, world.getWorldData())
                .createPaste(editSession, world.getWorldData())
                .to(new Vector(to.getX(), to.getY(), to.getZ()))
                .ignoreAirBlocks(false)
                .build();
        try {
            Operations.complete(operation);
        } catch (WorldEditException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean saveSchematic(Location primary, Location secondary, int id) {
        return saveSchematic(primary, secondary, getSchematicFile(String.valueOf(id)));
    }

    public static boolean saveSchematic(Location primary, Location secondary, File schematicFile) {
        if (!SCHEMATIC_DIR.exists()) SCHEMATIC_DIR.mkdirs();
        World world = new BukkitWorld(primary.getWorld());
        Vector primaryVector = new Vector(primary.getX(), primary.getY(), primary.getZ());
        Vector secondaryVector = new Vector(secondary.getX(), secondary.getY(), secondary.getZ());
        Region region = new CuboidRegion(primaryVector, secondaryVector);
        EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(world, -1);

        BlockArrayClipboard clipboard = new BlockArrayClipboard(region);
        ForwardExtentCopy copy = new ForwardExtentCopy(editSession, region, clipboard, region.getMinimumPoint());

        try {
            Operations.complete(copy);
        } catch (WorldEditException e) {
            throw new RuntimeException(e);
        }

        ClipboardFormat format = ClipboardFormat.SCHEMATIC;
        try (ClipboardWriter writer = format.getWriter(new FileOutputStream(schematicFile))) {
            writer.write(clipboard, world.getWorldData());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}