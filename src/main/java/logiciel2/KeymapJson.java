package logiciel2;

import java.util.List;
import java.util.Map;

/**
 * Représente le contenu de keymap.json,
 * c'est-à-dire la map "charToKeySequence":
 *   "A" -> ["Shift","a"]
 *   "ç" -> ["AltGr","c"]
 * etc.
 */
public class KeymapJson {
    public Map<String, List<String>> charToKeySequence;
}
