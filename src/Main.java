public class Main {


    //num sides of non-wild die for your shooting or whatever skill you are using
    static int tameSides = 10;
    static int toughness = 13;
    static int toHitBonus = 0;
    //num sides of wild die
    static int wildSides =6;


    static int weaponDieSides=4;
    static  int strengthDieSides=4;


    static int maxAllowedExplosions =8;//accurate to about 2 decimal places at 8

    static boolean wildAttack=true;
    static  boolean theDrop=true;
    static  boolean headShot=false;
    static  boolean joker=false;
    static  boolean deadShot=true;

    static boolean actions2=true;


    //don't touch these
    static boolean wildAdded=false;
    static boolean raise=false;
    static int explosionCount=0;
    public static void main(String[] args) {
        toughness-=toHitBonus;
        System.out.println("avg damage: " + avgDamage());

    }
    public static float avgDamage(){
        int bonusDamage=0;
        float countingDamage=0;
        if (theDrop){
            bonusDamage+=4;
            toughness-=4;
        }
        if (headShot) {
            bonusDamage += 4;
            toughness += 4;
        }
        if (joker){
            bonusDamage+=2;
            toughness-=2;
        }

        if(wildAttack || joker){
            bonusDamage+=2;
            toughness-=2;
        }
        if(actions2){
            toughness+=2;
        }


        wildAdded=false;
        float oddsNormal=roll(0);
        wildAdded=false;
        toughness+=4;
        float oddsRaise=roll(0);
        toughness-=4;
        countingDamage+=oddsRaise*explode(6);
        countingDamage+=(explode(weaponDieSides)+explode(strengthDieSides)+bonusDamage)*oddsNormal;


        if (deadShot&&joker){
            countingDamage*=2;
        }
        System.out.println("hit odds: " + oddsNormal*100 + "%");
        return countingDamage;
    }

    public static float explode(float numSides){
        explosionCount++;
        if (explosionCount < maxAllowedExplosions) {
            return (float) ((numSides / 2) + 0.5 + (1 / numSides) * explode(numSides));
        }
        else {
            explosionCount=0;
            return (float) ((numSides / 2)+.5);
        }
    }


    public static float roll(int multipleOfS){
        multipleOfS+= tameSides;

        if ((multipleOfS) < toughness){
            float tameResult=(1f/(float) tameSides) * roll(multipleOfS);
            float oddsOfNeedingWild=((float)tameSides -1)/(float)tameSides;
            float wildResult=0;
            if(wildAdded==false) {
                wildAdded=true;
                wildResult = oddsOfNeedingWild * wildDie(0);
            }
            return( tameResult+wildResult );
        }
        else if(multipleOfS==toughness){
            float wildResult=0;
            float oddsOfNeedingWild=((float)tameSides -1)/(float)tameSides;
            if(wildAdded==false) {
                wildAdded=true;
                wildResult = oddsOfNeedingWild * wildDie(0);
            }
            float tameResult=1/(float)tameSides;
            return (tameResult+wildResult);
        }
        else if (multipleOfS> toughness){
            float numUppers=(multipleOfS-toughness+1);
            float tameResult = numUppers/ (float) tameSides;
            float oddsOfNeedingWild=1-tameResult;
            float wildResult=0;
            if(wildAdded==false) {
                wildAdded=true;
                wildResult = oddsOfNeedingWild * wildDie(0);
            }
            return (tameResult + wildResult);
        }
        else{
            //error
            System.out.println("How r you here?");
            return (-1);
        }
    }

    public static float wildDie(int multipleOfS6){
        multipleOfS6+= wildSides;

        if (multipleOfS6 <toughness){
            return((1f/(float) wildSides) * wildDie(multipleOfS6));
        }
        else if(multipleOfS6==toughness){
            return (1f/(float)wildSides);
        }
        else{
            float numUppers=(multipleOfS6-toughness+1);
            return (numUppers/ (float) wildSides);
        }
    }

}