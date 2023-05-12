package codenames;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class WordGrid implements Serializable{
    private static final String[] WORDS = {
    		"tiger", "wing", "sail", "current", "vein", "meal", "driving", "scarf", "coach", "frog", "crib", "cheese", "sidewalk", "design", "apparel", "farm", "fact", "rings", "company", "office", "history", "scarecrow", "beds", "locket", "run", "plane", "queen", "building", "grass", "detail", "plants", "lettuce", "dirt", "vessel", "horses", "smash", "engine", "sister", "price", "soup", "cave", "string", "hands", "experience", "behavior", "drain", "scale", "sun", "flight", "earthquake", "growth", "aunt", "pail", "waste", "sign", "anger", "toe", "jellyfish", "marble", "tooth", "spade", "zephyr", "stove", "art", "badge", "dime", "impulse", "mailbox", "salt", "pie", "doctor", "nerve", "porter", "crate", "bushes", "tramp", "food", "downtown", "team", "bells", "lip", "silk", "glass", "reading", "rat", "pigs", "transport", "step", "rhythm", "mark", "boundary", "coast", "truck", "box", "test", "board", "arch", "mist", "sound", "trade", "lamp", "geese", "yoke", "view", "rate", "shock", "fish", "ear", "governor", "committee", "edge", "error", "condition", "leg", "table", "railway", "health", "plastic", "title", "offer", "train", "minute", "kick", "maid", "cushion", "fang", "shape", "letter", "cats", "card", "sink", "money", "grain", "poison", "wind", "debt", "mine", "carpenter", "account", "wound", "hope", "play", "rabbits", "basket", "berry", "hour", "tail", "attack", "plot", "eggs", "oranges", "bead", "bulb", "trouble", "letters", "home", "steel", "store", "power", "car", "cast", "thought", "screw", "women", "gun", "bucket", "toad", "iron", "clover", "government", "baseball", "bird", "cow", "hand", "fork", "sea", "rifle", "request", "tank", "nest", "eyes", "meat", "argument", "hydrant", "throat", "force", "activity", "tree", "wire", "shop", "grandmother", "paper", "quartz", "back", "stem", "grape", "jar", "scissors", "yak", "trucks", "brick", "event", "straw", "amusement", "punishment", "arithmetic", "harmony", "parcel", "pest", "passenger", "friction", "business", "man", "rub", "thumb", "head", "bait", "square", "door", "acoustics", "turkey", "jam", "flavor", "pocket", "glove", "pet", "bear", "range", "spot", "oil", "quicksand", "pleasure", "liquid", "addition", "hat", "reward", "profit", "cat", "knowledge", "teaching", "crime", "cart", "order", "wax", "aftermath", "mouth", "dock", "question", "pear", "waves", "cent", "skirt", "toothpaste", "cook", "ghost", "hair", "rest", "popcorn", "agreement", "tongue", "street", "magic", "person", "match", "curve", "scent", "pizzas", "teeth", "linen", "calendar", "name", "umbrella", "party", "wave", "selection", "pickle", "leather", "science", "vest", "cows", "motion", "flock", "plate", "seashore", "thunder", "judge", "reaction", "celery", "sofa", "vegetable", "hobbies", "snail", "pen", "writer", "seed", "toy", "cellar", "advertisement", "swing", "sort", "self", "ladybug", "change", "competition", "elbow", "development", "lunchroom", "ship", "quiet", "recess", "slave", "finger", "education", "soap", "cobweb", "hook", "story", "grandfather", "knee", "star", "creature", "adjustment", "amount", "sheet", "cars", "bikes", "jump", "apparatus", "tendency", "structure", "regret", "lake", "vacation", "school", "twig", "stretch", "cabbage", "actor", "instrument", "jail", "pin", "crown", "son", "fold", "care", "page", "country", "watch", "trail", "oatmeal", "mountain", "cause", "use", "toothbrush", "example", "van", "thread", "servant", "cream", "dress", "oven", "crayon", "hole", "distance", "arm", "approval", "talk", "pipe", "rule", "eggnog", "thrill", "reason", "trains", "wheel", "treatment", "drop", "prose", "market", "cub", "breath", "visitor", "potato", "credit", "hammer", "kittens", "calculator", "bell", "meeting", "rain", "zebra", "snakes", "gate", "chicken", "expansion", "beef", "protest", "invention", "library", "nut", "bite", "cap", "writing", "sock", "house", "rose", "root", "church", "laugh", "water", "relation", "trees", "giraffe", "deer", "middle", "death", "corn", "air", "month", "vase", "trousers", "hot", "collar", "eye", "limit", "slope", "interest", "woman", "mass", "bee", "discussion", "memory", "authority", "silver", "icicle", "zinc", "fireman", "bubble", "book", "room", "shelf", "muscle", "industry", "rabbit", "veil", "minister", "quill", "coal", "achiever", "kettle", "pollution", "look", "swim", "stone", "key", "suggestion", "wealth", "plough", "base", "heat", "love", "shoes", "tray", "camp", "harbor", "mother", "shirt", "grade", "wool", "blade", "juice", "brother", "afterthought", "frame", "honey", "butter", "friend", "guitar", "discovery", "religion", "circle", "system", "laborer", "monkey", "noise", "position", "action", "ticket", "dad", "kitty", "destruction", "can", "measure", "work", "planes", "fear", "tin", "boat", "advice", "expert", "voice", "cattle", "jeans", "produce", "girls", "field", "ink", "skin", "division", "drink", "stranger", "cemetery", "control", "quarter", "shake", "notebook", "seat", "squirrel", "exchange", "chalk", "stage", "desire", "kiss", "spring", "texture", "form", "distribution", "walk", "horse", "wash", "lace", "ice", "plant", "loaf", "road", "voyage", "steam", "playground", "frogs", "machine", "town", "border", "appliance", "attraction", "toys", "sense", "start", "ocean", "crack", "front", "increase", "shame", "volleyball", "smile", "idea", "nose", "blood", "dinosaurs", "peace", "crow", "fly", "flesh", "territory", "bed", "metal", "sticks", "song", "birth", "flowers", "quiver", "ground", "verse", "purpose", "wrench", "statement", "plantation", "tax", "space", "wall", "donkey", "stocking", "fall", "pump", "existence", "land", "cup", "crook", "way", "pot", "show", "hate", "duck", "need", "comparison", "support", "chin", "beginner", "mice", "branch", "balance", "dogs", "sand", "mom", "children", "chess", "observation", "end", "girl", "volcano", "toes", "pan", "airplane", "whip", "baby", "snow", "cannon", "cracker", "sugar", "caption", "humor", "spiders", "bike", "quilt", "foot", "afternoon", "summer", "battle", "dinner", "route", "believe", "tent", "cover", "unit", "things", "bridge", "club", "move", "chickens", "cactus", "belief", "babies", "top", "rice", "button", "direction", "record", "earth", "boy", "creator", "friends", "substance", "daughter", "smoke", "ants", "push", "neck", "hill", "snails", "rake", "wish", "fruit", "underwear", "boot", "canvas", "birds", "property", "part", "note", "lumber", "fire", "quince", "wine", "rod", "knife", "sleep", "twist", "ray", "payment", "legs", "wilderness", "time", "hospital", "knot", "cherry", "zipper", "sisters", "sweater", "spoon", "sneeze", "bomb", "effect", "basin", "grip", "thing", "fairies", "crowd", "chance", "bottle", "touch", "birthday", "coat", "roll", "rainstorm", "face", "size", "houses", "spark", "surprise", "airport", "books", "camera", "window", "day", "flag", "orange", "snake", "floor", "slip", "lock", "haircut", "cherries", "cake", "shoe", "low", "jelly", "fog", "sky", "act", "insect", "hose", "blow", "dog", "wrist", "wood", "stitch", "theory", "worm", "hall", "powder", "tub", "angle", "society", "pull", "uncle", "furniture", "guide", "bedroom", "bat", "soda", "zoo", "pets", "year", "station", "cloth", "burst", "nation", "loss", "river", "army", "cough", "spy", "yard", "north", "holiday", "sack", "decision", "respect", "night", "stick", "rail", "winter", "mitten", "doll", "sleet", "dolls", "stop", "wren", "income", "skate", "flame", "picture", "stomach", "word", "roof", "line", "stream", "curtain", "clam", "weather", "connection", "week", "mind", "desk", "pig", "digestion", "temper", "horn", "class", "stew", "giants", "pies", "representative", "riddle", "men", "shade", "gold", "secretary", "trick", "moon", "carriage", "trip", "degree", "needle", "ball", "cakes", "bone", "bag", "island", "bath", "brass", "group", "dust", "milk", "weight", "alarm", "color", "scene", "side", "partner", "suit", "lunch", "animal", "language", "cable", "morning", "bit", "taste", "pancake", "mask", "songs", "brake", "copper", "receipt", "tomatoes", "value", "jewel", "join", "level", "throne", "pencil", "ducks", "rock", "fowl", "yam", "sponge", "flower", "egg", "sheep", "point", "fuel", "insurance", "stamp", "coil", "mint", "feeling", "basketball", "whistle", "channel", "robin", "number", "drawer", "turn", "smell", "ring", "war", "yarn", "place"
    };
    private ArrayList<String> words;
    private Color[][] gridColors;
    private String[][] gridWords;
    ArrayList<String> wordList;

    public WordGrid() {
    	/*
    	 * The wordGrid serves as the grid that will be used in the client and in the server
    	 * */
        gridColors = new Color[5][5];
        gridWords = new String[5][5];
    }


    public WordGrid(String[] words) {
        gridWords = new String[5][5];
        gridColors = new Color[5][5];
        populateGrid(words);
    }
    
    public void populateGrid(String[] words) {
        wordList = new ArrayList<>(Arrays.asList(words));
        Collections.shuffle(wordList);
        
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                gridWords[i][j] = wordList.get(i*5 + j);
            }
        }
    }

    
    public void initializeGrid() {
        wordList = new ArrayList<>(Arrays.asList(WORDS));
        Collections.shuffle(wordList);
        
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                gridWords[i][j] = wordList.get(i*5 + j);
            }
        }
    }
    
    //new code added: initializeGrid that takes an array of words 

    public void initializeGrid(String[] newWords) {
        // Make sure the input array has the right size
        /*
    	if(newWords.length != 25){
            throw new IllegalArgumentException("The input array must contain exactly 25 words.");
        }
        */

        words = new ArrayList<>(Arrays.asList(newWords));
        Collections.shuffle(words);

        int index = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                gridWords[i][j] = words.get(index++);
            }
        }
    }
    
    //end of new code 

    
    public void assignColors() {
        ArrayList<Color> colors = new ArrayList<>();
        Random random = new Random();
        int redCount = random.nextBoolean() ? 8 : 7;
        int blueCount = 15 - redCount;

        for (int i = 0; i < redCount; i++) {
            colors.add(new Color(255, 171, 171, 255)); // Red with 50% opacity
        }
        for (int i = 0; i < blueCount; i++) {
            colors.add(new Color(171, 171, 255, 255)); // Blue with 50% opacity
        }

        for (int i = 0; i < 9; i++) {
            colors.add(new Color(245, 245, 220, 255)); // Beige color with 50% opacity
        }
        colors.add(new Color(171, 171, 171, 255)); // Dark Gray with 50% opacity

        Collections.shuffle(colors);

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                gridColors[i][j] = colors.get(i * 5 + j);
            }
        }
    }



    public Color[][] getGridColors() {
        return gridColors;
    }

    public String[][] getGridWords() {
        return gridWords;
    }
}
