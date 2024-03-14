namespace Laborator1;

partial class FormDirectorsMovies {
    /// <summary>
    ///  Required designer variable.
    /// </summary>
    private System.ComponentModel.IContainer components = null;

    /// <summary>
    ///  Clean up any resources being used.
    /// </summary>
    /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
    protected override void Dispose(bool disposing) {
        if (disposing && (components != null)) {
            components.Dispose();
        }

        base.Dispose(disposing);
    }

    #region Windows Form Designer generated code

    /// <summary>
    ///  Required method for Designer support - do not modify
    ///  the contents of this method with the code editor.
    /// </summary>
    private void InitializeComponent() {
        dataGridViewDirectors = new DataGridView();
        dataGridViewMovies = new DataGridView();
        labelMovieTitle = new Label();
        textBoxMovieTitle = new TextBox();
        labelMovieDirectorID = new Label();
        textBoxMovieDirectorID = new TextBox();
        labelMovieLaunchDate = new Label();
        textBoxMovieLaunchDate = new TextBox();
        labelMovieRunningTime = new Label();
        textBoxMovieRunningTime = new TextBox();
        buttonAddMovie = new Button();
        buttonUpdateMovie = new Button();
        buttonDeleteMovie = new Button();
        labelDirectors = new Label();
        labelMovies = new Label();
        ((System.ComponentModel.ISupportInitialize)dataGridViewDirectors).BeginInit();
        ((System.ComponentModel.ISupportInitialize)dataGridViewMovies).BeginInit();
        SuspendLayout();
        // 
        // dataGridViewDirectors
        // 
        dataGridViewDirectors.AllowUserToAddRows = false;
        dataGridViewDirectors.AllowUserToDeleteRows = false;
        dataGridViewDirectors.Anchor = AnchorStyles.Bottom | AnchorStyles.Left | AnchorStyles.Right;
        dataGridViewDirectors.ColumnHeadersHeightSizeMode = DataGridViewColumnHeadersHeightSizeMode.AutoSize;
        dataGridViewDirectors.EditMode = DataGridViewEditMode.EditProgrammatically;
        dataGridViewDirectors.Location = new Point(24, 193);
        dataGridViewDirectors.MultiSelect = false;
        dataGridViewDirectors.Name = "dataGridViewDirectors";
        dataGridViewDirectors.RowTemplate.Height = 25;
        dataGridViewDirectors.Size = new Size(353, 225);
        dataGridViewDirectors.TabIndex = 0;
        dataGridViewDirectors.CellMouseClick += dataGridViewDirectors_CellMouseClick;
        // 
        // dataGridViewMovies
        // 
        dataGridViewMovies.AllowUserToAddRows = false;
        dataGridViewMovies.AllowUserToDeleteRows = false;
        dataGridViewMovies.Anchor = AnchorStyles.Bottom | AnchorStyles.Right;
        dataGridViewMovies.ColumnHeadersHeightSizeMode = DataGridViewColumnHeadersHeightSizeMode.AutoSize;
        dataGridViewMovies.EditMode = DataGridViewEditMode.EditProgrammatically;
        dataGridViewMovies.Location = new Point(409, 93);
        dataGridViewMovies.MultiSelect = false;
        dataGridViewMovies.Name = "dataGridViewMovies";
        dataGridViewMovies.RowTemplate.Height = 25;
        dataGridViewMovies.Size = new Size(357, 325);
        dataGridViewMovies.TabIndex = 1;
        dataGridViewMovies.CellClick += dataGridViewMovies_CellClick;
        // 
        // labelMovieTitle
        // 
        labelMovieTitle.AccessibleName = "";
        labelMovieTitle.AutoSize = true;
        labelMovieTitle.Location = new Point(24, 27);
        labelMovieTitle.Name = "labelMovieTitle";
        labelMovieTitle.Size = new Size(66, 15);
        labelMovieTitle.TabIndex = 2;
        labelMovieTitle.Text = "Movie title:";
        // 
        // textBoxMovieTitle
        // 
        textBoxMovieTitle.Location = new Point(145, 24);
        textBoxMovieTitle.Name = "textBoxMovieTitle";
        textBoxMovieTitle.Size = new Size(132, 23);
        textBoxMovieTitle.TabIndex = 3;
        // 
        // labelMovieDirectorID
        // 
        labelMovieDirectorID.AccessibleName = "";
        labelMovieDirectorID.AutoSize = true;
        labelMovieDirectorID.Location = new Point(24, 60);
        labelMovieDirectorID.Name = "labelMovieDirectorID";
        labelMovieDirectorID.Size = new Size(101, 15);
        labelMovieDirectorID.TabIndex = 4;
        labelMovieDirectorID.Text = "Movie director ID:";
        // 
        // textBoxMovieDirectorID
        // 
        textBoxMovieDirectorID.Location = new Point(145, 57);
        textBoxMovieDirectorID.Name = "textBoxMovieDirectorID";
        textBoxMovieDirectorID.Size = new Size(132, 23);
        textBoxMovieDirectorID.TabIndex = 5;
        // 
        // labelMovieLaunchDate
        // 
        labelMovieLaunchDate.AccessibleName = "";
        labelMovieLaunchDate.AutoSize = true;
        labelMovieLaunchDate.Location = new Point(24, 93);
        labelMovieLaunchDate.Name = "labelMovieLaunchDate";
        labelMovieLaunchDate.Size = new Size(108, 15);
        labelMovieLaunchDate.TabIndex = 6;
        labelMovieLaunchDate.Text = "Movie launch date:";
        // 
        // textBoxMovieLaunchDate
        // 
        textBoxMovieLaunchDate.Location = new Point(145, 90);
        textBoxMovieLaunchDate.Name = "textBoxMovieLaunchDate";
        textBoxMovieLaunchDate.Size = new Size(132, 23);
        textBoxMovieLaunchDate.TabIndex = 7;
        // 
        // labelMovieRunningTime
        // 
        labelMovieRunningTime.AccessibleName = "";
        labelMovieRunningTime.AutoSize = true;
        labelMovieRunningTime.Location = new Point(24, 128);
        labelMovieRunningTime.Name = "labelMovieRunningTime";
        labelMovieRunningTime.Size = new Size(115, 15);
        labelMovieRunningTime.TabIndex = 8;
        labelMovieRunningTime.Text = "Movie running time:";
        // 
        // textBoxMovieRunningTime
        // 
        textBoxMovieRunningTime.Location = new Point(145, 125);
        textBoxMovieRunningTime.Name = "textBoxMovieRunningTime";
        textBoxMovieRunningTime.Size = new Size(132, 23);
        textBoxMovieRunningTime.TabIndex = 9;
        // 
        // buttonAddMovie
        // 
        buttonAddMovie.Location = new Point(339, 19);
        buttonAddMovie.Name = "buttonAddMovie";
        buttonAddMovie.Size = new Size(108, 30);
        buttonAddMovie.TabIndex = 10;
        buttonAddMovie.Text = "Add movie";
        buttonAddMovie.UseVisualStyleBackColor = true;
        // 
        // buttonUpdateMovie
        // 
        buttonUpdateMovie.Location = new Point(498, 19);
        buttonUpdateMovie.Name = "buttonUpdateMovie";
        buttonUpdateMovie.Size = new Size(108, 30);
        buttonUpdateMovie.TabIndex = 11;
        buttonUpdateMovie.Text = "Update movie";
        buttonUpdateMovie.UseVisualStyleBackColor = true;
        // 
        // buttonDeleteMovie
        // 
        buttonDeleteMovie.Location = new Point(658, 19);
        buttonDeleteMovie.Name = "buttonDeleteMovie";
        buttonDeleteMovie.Size = new Size(108, 30);
        buttonDeleteMovie.TabIndex = 12;
        buttonDeleteMovie.Text = "Delete movie";
        buttonDeleteMovie.UseVisualStyleBackColor = true;
        // 
        // labelDirectors
        // 
        labelDirectors.Anchor = AnchorStyles.Bottom | AnchorStyles.Left | AnchorStyles.Right;
        labelDirectors.AutoSize = true;
        labelDirectors.Location = new Point(168, 175);
        labelDirectors.Name = "labelDirectors";
        labelDirectors.Size = new Size(54, 15);
        labelDirectors.TabIndex = 13;
        labelDirectors.Text = "Directors";
        // 
        // labelMovies
        // 
        labelMovies.Anchor = AnchorStyles.Bottom | AnchorStyles.Right;
        labelMovies.AutoSize = true;
        labelMovies.Location = new Point(561, 75);
        labelMovies.Name = "labelMovies";
        labelMovies.Size = new Size(45, 15);
        labelMovies.TabIndex = 14;
        labelMovies.Text = "Movies";
        // 
        // FormDirectorsMovies
        // 
        AutoScaleDimensions = new SizeF(7F, 15F);
        AutoScaleMode = AutoScaleMode.Font;
        ClientSize = new Size(800, 450);
        Controls.Add(labelMovies);
        Controls.Add(labelDirectors);
        Controls.Add(buttonDeleteMovie);
        Controls.Add(buttonUpdateMovie);
        Controls.Add(buttonAddMovie);
        Controls.Add(textBoxMovieRunningTime);
        Controls.Add(labelMovieRunningTime);
        Controls.Add(textBoxMovieLaunchDate);
        Controls.Add(labelMovieLaunchDate);
        Controls.Add(textBoxMovieDirectorID);
        Controls.Add(labelMovieDirectorID);
        Controls.Add(textBoxMovieTitle);
        Controls.Add(labelMovieTitle);
        Controls.Add(dataGridViewMovies);
        Controls.Add(dataGridViewDirectors);
        Name = "FormDirectorsMovies";
        Text = "Directors & Movies";
        Load += Form1_Load;
        ((System.ComponentModel.ISupportInitialize)dataGridViewDirectors).EndInit();
        ((System.ComponentModel.ISupportInitialize)dataGridViewMovies).EndInit();
        ResumeLayout(false);
        PerformLayout();
    }

    #endregion

    private DataGridView dataGridViewDirectors;
    private DataGridView dataGridViewMovies;
    private Label labelMovieTitle;
    private TextBox textBoxMovieTitle;
    private Label labelMovieDirectorID;
    private TextBox textBoxMovieDirectorID;
    private Label labelMovieLaunchDate;
    private TextBox textBoxMovieLaunchDate;
    private Label labelMovieRunningTime;
    private TextBox textBoxMovieRunningTime;
    private Button buttonAddMovie;
    private Button buttonUpdateMovie;
    private Button buttonDeleteMovie;
    private Label labelDirectors;
    private Label labelMovies;
}