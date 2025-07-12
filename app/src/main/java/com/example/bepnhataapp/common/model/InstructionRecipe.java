package com.example.bepnhataapp.common.model;

public class InstructionRecipe {
    private long instructionID;
    private long recipeID;
    private int numberSection;
    private String title;
    private String content;
    private String image;

    public InstructionRecipe() {}

    public InstructionRecipe(long instructionID, long recipeID, int numberSection, String title, String content, String image) {
        this.instructionID = instructionID;
        this.recipeID = recipeID;
        this.numberSection = numberSection;
        this.title = title;
        this.content = content;
        this.image = image;
    }

    public long getInstructionID() { return instructionID; }
    public void setInstructionID(long instructionID) { this.instructionID = instructionID; }

    public long getRecipeID() { return recipeID; }
    public void setRecipeID(long recipeID) { this.recipeID = recipeID; }

    public int getNumberSection() { return numberSection; }
    public void setNumberSection(int numberSection) { this.numberSection = numberSection; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
} 
