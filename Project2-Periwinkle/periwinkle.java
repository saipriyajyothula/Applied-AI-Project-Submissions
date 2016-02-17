import nrc.fuzzy.*;

import java.io.*;
public class periwinkle
{
    public static void main(String[] argv) throws FuzzyException,IOException
    {
        RightLinearFunction rlf = new RightLinearFunction();
        LeftLinearFunction llf = new LeftLinearFunction();
        FuzzyValue showParacetamol = null, showAdvil = null, showBedrest = null, showDoc = null;
        FuzzyValueVector fvv = null, fvv2 = null;
        
        //Fever has terms mild, medium, high
        FuzzyVariable fever = new FuzzyVariable("Fever", 1, 10, "On a scale of 1-10");
        fever.addTerm("mild", new ZFuzzySet(1, 3));
        fever.addTerm("medium", new PIFuzzySet(4, 2));
        fever.addTerm("high", new SFuzzySet(5, 10));
        
        //Headache has terms mild, medium, severe
        FuzzyVariable headache = new FuzzyVariable("Headache", 1, 10, "On a scale of 1-10");
        headache.addTerm("mild", new ZFuzzySet(1, 3));
        headache.addTerm("medium", new PIFuzzySet(4, 2));
        headache.addTerm("severe", new SFuzzySet(5, 10));
        
        //Paracetamol dosage has terms low, medium, high (actual dosage measured in mg)
        FuzzyVariable paracetamol = new FuzzyVariable("Paracetamol", 150, 500, "mg");
        paracetamol.addTerm("low", new ZFuzzySet(150, 200));
        paracetamol.addTerm("medium", new PIFuzzySet(250, 65));
        paracetamol.addTerm("high", new SFuzzySet(300, 500));
        
        //Advil dosage has terms low, medium, high (actual dosage measured in mg)
        FuzzyVariable advil = new FuzzyVariable("Advil", 100, 400, "mg");
        advil.addTerm("low", new ZFuzzySet(100, 200));
        advil.addTerm("medium", new PIFuzzySet(240, 50));
        advil.addTerm("high", new SFuzzySet(280, 400));
        
        //Bed rest is measured in hours and has terms low, medium, high
        FuzzyVariable bedrest = new FuzzyVariable("Bed rest", 1, 10, "hours");
        bedrest.addTerm("low", new RFuzzySet(1, 3, rlf));
        bedrest.addTerm("medium", new RectangleFuzzySet(2, 5));
        bedrest.addTerm("high", new LFuzzySet(5, 10, llf));
        
        //Suggesting a doctor - no or yes
        FuzzyVariable doc = new FuzzyVariable("Doctor", 0, 1, "No or Yes");
        doc.addTerm("no", new SingletonFuzzySet(0));
        doc.addTerm("yes", new SingletonFuzzySet(1));
        
        //Rule 1
        FuzzyRule mildFeverMildHeadache = new FuzzyRule();
        mildFeverMildHeadache.addAntecedent(new FuzzyValue(fever, "mild"));
        mildFeverMildHeadache.addAntecedent(new FuzzyValue(headache, "mild"));
        mildFeverMildHeadache.addConclusion(new FuzzyValue(paracetamol, "low"));
        mildFeverMildHeadache.addConclusion(new FuzzyValue(advil, "low"));
        mildFeverMildHeadache.addConclusion(new FuzzyValue(bedrest, "low"));
        
        //Rule 2
        FuzzyRule mildFeverMediumHeadache = new FuzzyRule();
        mildFeverMediumHeadache.addAntecedent(new FuzzyValue(fever, "mild"));
        mildFeverMediumHeadache.addAntecedent(new FuzzyValue(headache, "medium"));
        mildFeverMediumHeadache.addConclusion(new FuzzyValue(paracetamol, "low"));
        mildFeverMediumHeadache.addConclusion(new FuzzyValue(advil, "medium"));
        mildFeverMediumHeadache.addConclusion(new FuzzyValue(bedrest, "low"));
        
        //Rule 3
        FuzzyRule mildFeverSevereHeadache = new FuzzyRule();
        mildFeverSevereHeadache.addAntecedent(new FuzzyValue(fever, "mild"));
        mildFeverSevereHeadache.addAntecedent(new FuzzyValue(headache, "severe"));
        mildFeverSevereHeadache.addConclusion(new FuzzyValue(paracetamol, "low"));
        mildFeverSevereHeadache.addConclusion(new FuzzyValue(advil, "high"));
        mildFeverSevereHeadache.addConclusion(new FuzzyValue(bedrest, "medium"));
        
        //Rule 4
        FuzzyRule mediumFeverMildHeadache = new FuzzyRule();
        mediumFeverMildHeadache.addAntecedent(new FuzzyValue(fever, "medium"));
        mediumFeverMildHeadache.addAntecedent(new FuzzyValue(headache, "mild"));
        mediumFeverMildHeadache.addConclusion(new FuzzyValue(paracetamol, "medium"));
        mediumFeverMildHeadache.addConclusion(new FuzzyValue(advil, "low"));
        mediumFeverMildHeadache.addConclusion(new FuzzyValue(bedrest, "medium"));
        
        //Rule 5
        FuzzyRule mediumFeverMediumHeadache = new FuzzyRule();
        mediumFeverMediumHeadache.addAntecedent(new FuzzyValue(fever, "medium"));
        mediumFeverMediumHeadache.addAntecedent(new FuzzyValue(headache, "medium"));
        mediumFeverMediumHeadache.addConclusion(new FuzzyValue(paracetamol, "medium"));
        mediumFeverMediumHeadache.addConclusion(new FuzzyValue(advil, "medium"));
        mediumFeverMediumHeadache.addConclusion(new FuzzyValue(bedrest, "medium"));
        
        //Rule 6
        FuzzyRule mediumFeverSevereHeadache = new FuzzyRule();
        mediumFeverSevereHeadache.addAntecedent(new FuzzyValue(fever, "medium"));
        mediumFeverSevereHeadache.addAntecedent(new FuzzyValue(headache, "severe"));
        mediumFeverSevereHeadache.addConclusion(new FuzzyValue(paracetamol, "medium"));
        mediumFeverSevereHeadache.addConclusion(new FuzzyValue(advil, "high"));
        mediumFeverSevereHeadache.addConclusion(new FuzzyValue(bedrest, "medium"));
        
        //Rule 7
        FuzzyRule highFeverMildHeadache = new FuzzyRule();
        highFeverMildHeadache.addAntecedent(new FuzzyValue(fever, "high"));
        highFeverMildHeadache.addAntecedent(new FuzzyValue(headache, "mild"));
        highFeverMildHeadache.addConclusion(new FuzzyValue(paracetamol, "high"));
        highFeverMildHeadache.addConclusion(new FuzzyValue(advil, "low"));
        highFeverMildHeadache.addConclusion(new FuzzyValue(bedrest, "high"));
        
        //Rule 8
        FuzzyRule highFeverMediumHeadache = new FuzzyRule();
        highFeverMediumHeadache.addAntecedent(new FuzzyValue(fever, "high"));
        highFeverMediumHeadache.addAntecedent(new FuzzyValue(headache, "medium"));
        highFeverMediumHeadache.addConclusion(new FuzzyValue(paracetamol, "high"));
        highFeverMediumHeadache.addConclusion(new FuzzyValue(advil, "medium"));
        highFeverMediumHeadache.addConclusion(new FuzzyValue(bedrest, "high"));
        
        //Rule 9
        FuzzyRule highFeverSevereHeadache = new FuzzyRule();
        highFeverSevereHeadache.addAntecedent(new FuzzyValue(fever, "high"));
        highFeverSevereHeadache.addAntecedent(new FuzzyValue(headache, "severe"));
        highFeverSevereHeadache.addConclusion(new FuzzyValue(paracetamol, "high"));
        highFeverSevereHeadache.addConclusion(new FuzzyValue(advil, "high"));
        highFeverSevereHeadache.addConclusion(new FuzzyValue(bedrest, "high"));
        
        //Rule 10 - Suggesting a doctor is not necessary
        FuzzyRule noDoc = new FuzzyRule();
        noDoc.addAntecedent(new FuzzyValue(bedrest, "low"));
        noDoc.addConclusion(new FuzzyValue(doc, "no"));
        
        //Rule 11 - Suggest a doctor when the severity of both fever and headache is more (bed rest medium)
        FuzzyRule yesDocM = new FuzzyRule();
        yesDocM.addAntecedent(new FuzzyValue(bedrest, "medium"));
        yesDocM.addConclusion(new FuzzyValue(doc, "yes"));
        
        //Rule 12 - Suggest a doctor when the severity of both fever and headache is more (bed rest high)
        FuzzyRule yesDocH = new FuzzyRule();
        yesDocH.addAntecedent(new FuzzyValue(bedrest, "high"));
        yesDocH.addConclusion(new FuzzyValue(doc, "yes"));
        
        //Take inputs
        double f, h;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("ALL INFERENCES ARE ILLUSTRATIVE.\n");
        System.out.println("On a scale of 1 to 10, how severe is your fever?");
        f = Double.parseDouble(br.readLine());
        System.out.println("On a scale of 1 to 10, what is the severity of your headache?");
        h = Double.parseDouble(br.readLine());
        FuzzyValue inputFever =  new FuzzyValue(fever, new SingletonFuzzySet(f));
        FuzzyValue inputHeadache =  new FuzzyValue(headache, new SingletonFuzzySet(h));
        
        //Fire rules
        mildFeverMildHeadache.removeAllInputs();
        mildFeverMildHeadache.addInput(inputFever);
        mildFeverMildHeadache.addInput(inputHeadache);
        mildFeverMediumHeadache.removeAllInputs();
        mildFeverMediumHeadache.addInput(inputFever);
        mildFeverMediumHeadache.addInput(inputHeadache);
        mildFeverSevereHeadache.removeAllInputs();
        mildFeverSevereHeadache.addInput(inputFever);
        mildFeverSevereHeadache.addInput(inputHeadache);
        
        mediumFeverMildHeadache.removeAllInputs();
        mediumFeverMildHeadache.addInput(inputFever);
        mediumFeverMildHeadache.addInput(inputHeadache);
        mediumFeverMediumHeadache.removeAllInputs();
        mediumFeverMediumHeadache.addInput(inputFever);
        mediumFeverMediumHeadache.addInput(inputHeadache);
        mediumFeverSevereHeadache.removeAllInputs();
        mediumFeverSevereHeadache.addInput(inputFever);
        mediumFeverSevereHeadache.addInput(inputHeadache);
        
        highFeverMildHeadache.removeAllInputs();
        highFeverMildHeadache.addInput(inputFever);
        highFeverMildHeadache.addInput(inputHeadache);
        highFeverMediumHeadache.removeAllInputs();
        highFeverMediumHeadache.addInput(inputFever);
        highFeverMediumHeadache.addInput(inputHeadache);
        highFeverSevereHeadache.removeAllInputs();
        highFeverSevereHeadache.addInput(inputFever);
        highFeverSevereHeadache.addInput(inputHeadache);
        
        //Conditions and fuzzy unions
        if(mildFeverMildHeadache.testRuleMatching())
        {
            fvv=mildFeverMildHeadache.execute();
            if (showParacetamol == null)
                showParacetamol = fvv.fuzzyValueAt(0);
            else
                showParacetamol = showParacetamol.fuzzyUnion(fvv.fuzzyValueAt(0));
            if (showAdvil == null)
                showAdvil = fvv.fuzzyValueAt(1);
            else
                showAdvil = showAdvil.fuzzyUnion(fvv.fuzzyValueAt(1));
            if (showBedrest == null)
                showBedrest = fvv.fuzzyValueAt(2);
            else
                showBedrest = showBedrest.fuzzyUnion(fvv.fuzzyValueAt(2));
            
            noDoc.removeAllInputs();
            noDoc.addInput(showBedrest);
            yesDocM.removeAllInputs();
            yesDocM.addInput(showBedrest);
            yesDocH.removeAllInputs();
            yesDocH.addInput(showBedrest);
            
            if(noDoc.testRuleMatching())
            {
                fvv2=noDoc.execute();
                if (showDoc == null)
                    showDoc = fvv2.fuzzyValueAt(0);
                else
                    showDoc = showDoc.fuzzyUnion(fvv2.fuzzyValueAt(0));
            }
            if(yesDocM.testRuleMatching())
            {
                fvv2=yesDocM.execute();
                if (showDoc == null)
                    showDoc = fvv2.fuzzyValueAt(0);
                else
                    showDoc = showDoc.fuzzyUnion(fvv2.fuzzyValueAt(0));
            }
            if(yesDocH.testRuleMatching())
            {
                fvv2=yesDocH.execute();
                if (showDoc == null)
                    showDoc = fvv2.fuzzyValueAt(0);
                else
                    showDoc = showDoc.fuzzyUnion(fvv2.fuzzyValueAt(0));
            }
        }
        
        if(mildFeverMediumHeadache.testRuleMatching())
        {
            fvv=mildFeverMediumHeadache.execute();
            if (showParacetamol == null)
                showParacetamol = fvv.fuzzyValueAt(0);
            else
                showParacetamol = showParacetamol.fuzzyUnion(fvv.fuzzyValueAt(0));
            if (showAdvil == null)
                showAdvil = fvv.fuzzyValueAt(1);
            else
                showAdvil = showAdvil.fuzzyUnion(fvv.fuzzyValueAt(1));
            if (showBedrest == null)
                showBedrest = fvv.fuzzyValueAt(2);
            else
                showBedrest = showBedrest.fuzzyUnion(fvv.fuzzyValueAt(2));
            
            noDoc.removeAllInputs();
            noDoc.addInput(showBedrest);
            yesDocM.removeAllInputs();
            yesDocM.addInput(showBedrest);
            yesDocH.removeAllInputs();
            yesDocH.addInput(showBedrest);
            
            if(noDoc.testRuleMatching())
            {
                fvv2=noDoc.execute();
                if (showDoc == null)
                    showDoc = fvv2.fuzzyValueAt(0);
                else
                    showDoc = showDoc.fuzzyUnion(fvv2.fuzzyValueAt(0));
            }
            if(yesDocM.testRuleMatching())
            {
                fvv2=yesDocM.execute();
                if (showDoc == null)
                    showDoc = fvv2.fuzzyValueAt(0);
                else
                    showDoc = showDoc.fuzzyUnion(fvv2.fuzzyValueAt(0));
            }
            if(yesDocH.testRuleMatching())
            {
                fvv2=yesDocH.execute();
                if (showDoc == null)
                    showDoc = fvv2.fuzzyValueAt(0);
                else
                    showDoc = showDoc.fuzzyUnion(fvv2.fuzzyValueAt(0));
            }
        }
        
        if(mildFeverSevereHeadache.testRuleMatching())
        {
            fvv=mildFeverSevereHeadache.execute();
            if (showParacetamol == null)
                showParacetamol = fvv.fuzzyValueAt(0);
            else
                showParacetamol = showParacetamol.fuzzyUnion(fvv.fuzzyValueAt(0));
            if (showAdvil == null)
                showAdvil = fvv.fuzzyValueAt(1);
            else
                showAdvil = showAdvil.fuzzyUnion(fvv.fuzzyValueAt(1));
            if (showBedrest == null)
                showBedrest = fvv.fuzzyValueAt(2);
            else
                showBedrest = showBedrest.fuzzyUnion(fvv.fuzzyValueAt(2));
            
            noDoc.removeAllInputs();
            noDoc.addInput(showBedrest);
            yesDocM.removeAllInputs();
            yesDocM.addInput(showBedrest);
            yesDocH.removeAllInputs();
            yesDocH.addInput(showBedrest);
            
            if(noDoc.testRuleMatching())
            {
                fvv2=noDoc.execute();
                if (showDoc == null)
                    showDoc = fvv2.fuzzyValueAt(0);
                else
                    showDoc = showDoc.fuzzyUnion(fvv2.fuzzyValueAt(0));
            }
            if(yesDocM.testRuleMatching())
            {
                fvv2=yesDocM.execute();
                if (showDoc == null)
                    showDoc = fvv2.fuzzyValueAt(0);
                else
                    showDoc = showDoc.fuzzyUnion(fvv2.fuzzyValueAt(0));
            }
            if(yesDocH.testRuleMatching())
            {
                fvv2=yesDocH.execute();
                if (showDoc == null)
                    showDoc = fvv2.fuzzyValueAt(0);
                else
                    showDoc = showDoc.fuzzyUnion(fvv2.fuzzyValueAt(0));
            }
        }
        
        if(mediumFeverMildHeadache.testRuleMatching())
        {
            fvv=mediumFeverMildHeadache.execute();
            if (showParacetamol == null)
                showParacetamol = fvv.fuzzyValueAt(0);
            else
                showParacetamol = showParacetamol.fuzzyUnion(fvv.fuzzyValueAt(0));
            if (showAdvil == null)
                showAdvil = fvv.fuzzyValueAt(1);
            else
                showAdvil = showAdvil.fuzzyUnion(fvv.fuzzyValueAt(1));
            if (showBedrest == null)
                showBedrest = fvv.fuzzyValueAt(2);
            else
                showBedrest = showBedrest.fuzzyUnion(fvv.fuzzyValueAt(2));
            
            noDoc.removeAllInputs();
            noDoc.addInput(showBedrest);
            yesDocM.removeAllInputs();
            yesDocM.addInput(showBedrest);
            yesDocH.removeAllInputs();
            yesDocH.addInput(showBedrest);
            
            if(noDoc.testRuleMatching())
            {
                fvv2=noDoc.execute();
                if (showDoc == null)
                    showDoc = fvv2.fuzzyValueAt(0);
                else
                    showDoc = showDoc.fuzzyUnion(fvv2.fuzzyValueAt(0));
            }
            if(yesDocM.testRuleMatching())
            {
                fvv2=yesDocM.execute();
                if (showDoc == null)
                    showDoc = fvv2.fuzzyValueAt(0);
                else
                    showDoc = showDoc.fuzzyUnion(fvv2.fuzzyValueAt(0));
            }
            if(yesDocH.testRuleMatching())
            {
                fvv2=yesDocH.execute();
                if (showDoc == null)
                    showDoc = fvv2.fuzzyValueAt(0);
                else
                    showDoc = showDoc.fuzzyUnion(fvv2.fuzzyValueAt(0));
            }
        }
        
        if(mediumFeverMediumHeadache.testRuleMatching())
        {
            fvv=mediumFeverMediumHeadache.execute();
            if (showParacetamol == null)
                showParacetamol = fvv.fuzzyValueAt(0);
            else
                showParacetamol = showParacetamol.fuzzyUnion(fvv.fuzzyValueAt(0));
            if (showAdvil == null)
                showAdvil = fvv.fuzzyValueAt(1);
            else
                showAdvil = showAdvil.fuzzyUnion(fvv.fuzzyValueAt(1));
            if (showBedrest == null)
                showBedrest = fvv.fuzzyValueAt(2);
            else
                showBedrest = showBedrest.fuzzyUnion(fvv.fuzzyValueAt(2));
            
            noDoc.removeAllInputs();
            noDoc.addInput(showBedrest);
            yesDocM.removeAllInputs();
            yesDocM.addInput(showBedrest);
            yesDocH.removeAllInputs();
            yesDocH.addInput(showBedrest);
            
            if(noDoc.testRuleMatching())
            {
                fvv2=noDoc.execute();
                if (showDoc == null)
                    showDoc = fvv2.fuzzyValueAt(0);
                else
                    showDoc = showDoc.fuzzyUnion(fvv2.fuzzyValueAt(0));
            }
            if(yesDocM.testRuleMatching())
            {
                fvv2=yesDocM.execute();
                if (showDoc == null)
                    showDoc = fvv2.fuzzyValueAt(0);
                else
                    showDoc = showDoc.fuzzyUnion(fvv2.fuzzyValueAt(0));
            }
            if(yesDocH.testRuleMatching())
            {
                fvv2=yesDocH.execute();
                if (showDoc == null)
                    showDoc = fvv2.fuzzyValueAt(0);
                else
                    showDoc = showDoc.fuzzyUnion(fvv2.fuzzyValueAt(0));
            }
        }
        
        if(mediumFeverSevereHeadache.testRuleMatching())
        {
            fvv=mediumFeverSevereHeadache.execute();
            if (showParacetamol == null)
                showParacetamol = fvv.fuzzyValueAt(0);
            else
                showParacetamol = showParacetamol.fuzzyUnion(fvv.fuzzyValueAt(0));
            if (showAdvil == null)
                showAdvil = fvv.fuzzyValueAt(1);
            else
                showAdvil = showAdvil.fuzzyUnion(fvv.fuzzyValueAt(1));
            if (showBedrest == null)
                showBedrest = fvv.fuzzyValueAt(2);
            else
                showBedrest = showBedrest.fuzzyUnion(fvv.fuzzyValueAt(2));
            
            noDoc.removeAllInputs();
            noDoc.addInput(showBedrest);
            yesDocM.removeAllInputs();
            yesDocM.addInput(showBedrest);
            yesDocH.removeAllInputs();
            yesDocH.addInput(showBedrest);
            
            if(noDoc.testRuleMatching())
            {
                fvv2=noDoc.execute();
                if (showDoc == null)
                    showDoc = fvv2.fuzzyValueAt(0);
                else
                    showDoc = showDoc.fuzzyUnion(fvv2.fuzzyValueAt(0));
            }
            if(yesDocM.testRuleMatching())
            {
                fvv2=yesDocM.execute();
                if (showDoc == null)
                    showDoc = fvv2.fuzzyValueAt(0);
                else
                    showDoc = showDoc.fuzzyUnion(fvv2.fuzzyValueAt(0));
            }
            if(yesDocH.testRuleMatching())
            {
                fvv2=yesDocH.execute();
                if (showDoc == null)
                    showDoc = fvv2.fuzzyValueAt(0);
                else
                    showDoc = showDoc.fuzzyUnion(fvv2.fuzzyValueAt(0));
            }
        }
        
        if(highFeverMildHeadache.testRuleMatching())
        {
            fvv=highFeverMildHeadache.execute();
            if (showParacetamol == null)
                showParacetamol = fvv.fuzzyValueAt(0);
            else
                showParacetamol = showParacetamol.fuzzyUnion(fvv.fuzzyValueAt(0));
            if (showAdvil == null)
                showAdvil = fvv.fuzzyValueAt(1);
            else
                showAdvil = showAdvil.fuzzyUnion(fvv.fuzzyValueAt(1));
            if (showBedrest == null)
                showBedrest = fvv.fuzzyValueAt(2);
            else
                showBedrest = showBedrest.fuzzyUnion(fvv.fuzzyValueAt(2));
            
            noDoc.removeAllInputs();
            noDoc.addInput(showBedrest);
            yesDocM.removeAllInputs();
            yesDocM.addInput(showBedrest);
            yesDocH.removeAllInputs();
            yesDocH.addInput(showBedrest);
            
            if(noDoc.testRuleMatching())
            {
                fvv2=noDoc.execute();
                if (showDoc == null)
                    showDoc = fvv2.fuzzyValueAt(0);
                else
                    showDoc = showDoc.fuzzyUnion(fvv2.fuzzyValueAt(0));
            }
            if(yesDocM.testRuleMatching())
            {
                fvv2=yesDocM.execute();
                if (showDoc == null)
                    showDoc = fvv2.fuzzyValueAt(0);
                else
                    showDoc = showDoc.fuzzyUnion(fvv2.fuzzyValueAt(0));
            }
            if(yesDocH.testRuleMatching())
            {
                fvv2=yesDocH.execute();
                if (showDoc == null)
                    showDoc = fvv2.fuzzyValueAt(0);
                else
                    showDoc = showDoc.fuzzyUnion(fvv2.fuzzyValueAt(0));
            }
        }
        
        if(highFeverMediumHeadache.testRuleMatching())
        {
            fvv=highFeverMediumHeadache.execute();
            if (showParacetamol == null)
                showParacetamol = fvv.fuzzyValueAt(0);
            else
                showParacetamol = showParacetamol.fuzzyUnion(fvv.fuzzyValueAt(0));
            if (showAdvil == null)
                showAdvil = fvv.fuzzyValueAt(1);
            else
                showAdvil = showAdvil.fuzzyUnion(fvv.fuzzyValueAt(1));
            if (showBedrest == null)
                showBedrest = fvv.fuzzyValueAt(2);
            else
                showBedrest = showBedrest.fuzzyUnion(fvv.fuzzyValueAt(2));
            
            noDoc.removeAllInputs();
            noDoc.addInput(showBedrest);
            yesDocM.removeAllInputs();
            yesDocM.addInput(showBedrest);
            yesDocH.removeAllInputs();
            yesDocH.addInput(showBedrest);
            
            if(noDoc.testRuleMatching())
            {
                fvv2=noDoc.execute();
                if (showDoc == null)
                    showDoc = fvv2.fuzzyValueAt(0);
                else
                    showDoc = showDoc.fuzzyUnion(fvv2.fuzzyValueAt(0));
            }
            if(yesDocM.testRuleMatching())
            {
                fvv2=yesDocM.execute();
                if (showDoc == null)
                    showDoc = fvv2.fuzzyValueAt(0);
                else
                    showDoc = showDoc.fuzzyUnion(fvv2.fuzzyValueAt(0));
            }
            if(yesDocH.testRuleMatching())
            {
                fvv2=yesDocH.execute();
                if (showDoc == null)
                    showDoc = fvv2.fuzzyValueAt(0);
                else
                    showDoc = showDoc.fuzzyUnion(fvv2.fuzzyValueAt(0));
            }
        }
        
        if(highFeverSevereHeadache.testRuleMatching())
        {
            fvv=highFeverSevereHeadache.execute();
            if (showParacetamol == null)
                showParacetamol = fvv.fuzzyValueAt(0);
            else
                showParacetamol = showParacetamol.fuzzyUnion(fvv.fuzzyValueAt(0));
            if (showAdvil == null)
                showAdvil = fvv.fuzzyValueAt(1);
            else
                showAdvil = showAdvil.fuzzyUnion(fvv.fuzzyValueAt(1));
            if (showBedrest == null)
                showBedrest = fvv.fuzzyValueAt(2);
            else
                showBedrest = showBedrest.fuzzyUnion(fvv.fuzzyValueAt(2));
            
            noDoc.removeAllInputs();
            noDoc.addInput(showBedrest);
            yesDocM.removeAllInputs();
            yesDocM.addInput(showBedrest);
            yesDocH.removeAllInputs();
            yesDocH.addInput(showBedrest);
            
            if(noDoc.testRuleMatching())
            {
                fvv2=noDoc.execute();
                if (showDoc == null)
                    showDoc = fvv2.fuzzyValueAt(0);
                else
                    showDoc = showDoc.fuzzyUnion(fvv2.fuzzyValueAt(0));
            }
            if(yesDocM.testRuleMatching())
            {
                fvv2=yesDocM.execute();
                if (showDoc == null)
                    showDoc = fvv2.fuzzyValueAt(0);
                else
                    showDoc = showDoc.fuzzyUnion(fvv2.fuzzyValueAt(0));
            }
            if(yesDocH.testRuleMatching())
            {
                fvv2=yesDocH.execute();
                if (showDoc == null)
                    showDoc = fvv2.fuzzyValueAt(0);
                else
                    showDoc = showDoc.fuzzyUnion(fvv2.fuzzyValueAt(0));
            }
        }
        
        //Defuzzifying fuzzy values
        double p = showParacetamol.weightedAverageDefuzzify();
        p = Math.round(p/10)*10;
        double a = showAdvil.weightedAverageDefuzzify();
        a = Math.round(a/10)*10;
        double hr = Math.round(showBedrest.momentDefuzzify());
        double sDoc = showDoc.weightedAverageDefuzzify();
        
        //Output the results
        System.out.print("\nI prescribe " + p + " mg of Paracetamol and " + a + " mg of Advil. I suggest that you ");
        if(sDoc == 1) {
            System.out.print("consult your doctor immediately and ");}
        System.out.println("take " + hr + " hours of bed rest.");
    }  
}
