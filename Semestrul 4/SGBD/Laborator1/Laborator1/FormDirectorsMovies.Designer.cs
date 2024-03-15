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
        labelMovieRunningTime = new Label();
        textBoxMovieRunningTime = new TextBox();
        buttonAddMovie = new Button();
        buttonUpdateMovie = new Button();
        buttonDeleteMovie = new Button();
        labelDirectors = new Label();
        labelMovies = new Label();
        dateTimePickerMovieLaunchDate = new DateTimePicker();
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
        dataGridViewDirectors.Location = new Point(34, 322);
        dataGridViewDirectors.Margin = new Padding(4, 5, 4, 5);
        dataGridViewDirectors.MaximumSize = new Size(500, 400);
        dataGridViewDirectors.MinimumSize = new Size(200, 350);
        dataGridViewDirectors.MultiSelect = false;
        dataGridViewDirectors.Name = "dataGridViewDirectors";
        dataGridViewDirectors.RowHeadersWidth = 62;
        dataGridViewDirectors.RowTemplate.Height = 25;
        dataGridViewDirectors.Size = new Size(500, 350);
        dataGridViewDirectors.TabIndex = 0;
        dataGridViewDirectors.CellClick += dataGridViewDirectors_CellClick;
        // 
        // dataGridViewMovies
        // 
        dataGridViewMovies.AllowUserToAddRows = false;
        dataGridViewMovies.AllowUserToDeleteRows = false;
        dataGridViewMovies.Anchor = AnchorStyles.Bottom | AnchorStyles.Right;
        dataGridViewMovies.ColumnHeadersHeightSizeMode = DataGridViewColumnHeadersHeightSizeMode.AutoSize;
        dataGridViewMovies.EditMode = DataGridViewEditMode.EditProgrammatically;
        dataGridViewMovies.Location = new Point(584, 155);
        dataGridViewMovies.Margin = new Padding(4, 5, 4, 5);
        dataGridViewMovies.MultiSelect = false;
        dataGridViewMovies.Name = "dataGridViewMovies";
        dataGridViewMovies.RowHeadersWidth = 62;
        dataGridViewMovies.RowTemplate.Height = 25;
        dataGridViewMovies.Size = new Size(510, 542);
        dataGridViewMovies.TabIndex = 1;
        dataGridViewMovies.CellClick += dataGridViewMovies_CellClick;
        // 
        // labelMovieTitle
        // 
        labelMovieTitle.AccessibleName = "";
        labelMovieTitle.AutoSize = true;
        labelMovieTitle.Location = new Point(34, 45);
        labelMovieTitle.Margin = new Padding(4, 0, 4, 0);
        labelMovieTitle.Name = "labelMovieTitle";
        labelMovieTitle.Size = new Size(99, 25);
        labelMovieTitle.TabIndex = 2;
        labelMovieTitle.Text = "Movie title:";
        // 
        // textBoxMovieTitle
        // 
        textBoxMovieTitle.Location = new Point(207, 40);
        textBoxMovieTitle.Margin = new Padding(4, 5, 4, 5);
        textBoxMovieTitle.Name = "textBoxMovieTitle";
        textBoxMovieTitle.Size = new Size(187, 31);
        textBoxMovieTitle.TabIndex = 3;
        // 
        // labelMovieDirectorID
        // 
        labelMovieDirectorID.AccessibleName = "";
        labelMovieDirectorID.AutoSize = true;
        labelMovieDirectorID.Location = new Point(34, 100);
        labelMovieDirectorID.Margin = new Padding(4, 0, 4, 0);
        labelMovieDirectorID.Name = "labelMovieDirectorID";
        labelMovieDirectorID.Size = new Size(154, 25);
        labelMovieDirectorID.TabIndex = 4;
        labelMovieDirectorID.Text = "Movie director ID:";
        // 
        // textBoxMovieDirectorID
        // 
        textBoxMovieDirectorID.Enabled = false;
        textBoxMovieDirectorID.Location = new Point(207, 95);
        textBoxMovieDirectorID.Margin = new Padding(4, 5, 4, 5);
        textBoxMovieDirectorID.Name = "textBoxMovieDirectorID";
        textBoxMovieDirectorID.Size = new Size(187, 31);
        textBoxMovieDirectorID.TabIndex = 5;
        // 
        // labelMovieLaunchDate
        // 
        labelMovieLaunchDate.AccessibleName = "";
        labelMovieLaunchDate.AutoSize = true;
        labelMovieLaunchDate.Location = new Point(34, 155);
        labelMovieLaunchDate.Margin = new Padding(4, 0, 4, 0);
        labelMovieLaunchDate.Name = "labelMovieLaunchDate";
        labelMovieLaunchDate.Size = new Size(161, 25);
        labelMovieLaunchDate.TabIndex = 6;
        labelMovieLaunchDate.Text = "Movie launch date:";
        // 
        // labelMovieRunningTime
        // 
        labelMovieRunningTime.AccessibleName = "";
        labelMovieRunningTime.AutoSize = true;
        labelMovieRunningTime.Location = new Point(34, 213);
        labelMovieRunningTime.Margin = new Padding(4, 0, 4, 0);
        labelMovieRunningTime.Name = "labelMovieRunningTime";
        labelMovieRunningTime.Size = new Size(171, 25);
        labelMovieRunningTime.TabIndex = 8;
        labelMovieRunningTime.Text = "Movie running time:";
        // 
        // textBoxMovieRunningTime
        // 
        textBoxMovieRunningTime.Location = new Point(207, 208);
        textBoxMovieRunningTime.Margin = new Padding(4, 5, 4, 5);
        textBoxMovieRunningTime.Name = "textBoxMovieRunningTime";
        textBoxMovieRunningTime.Size = new Size(187, 31);
        textBoxMovieRunningTime.TabIndex = 9;
        // 
        // buttonAddMovie
        // 
        buttonAddMovie.Location = new Point(484, 32);
        buttonAddMovie.Margin = new Padding(4, 5, 4, 5);
        buttonAddMovie.Name = "buttonAddMovie";
        buttonAddMovie.Size = new Size(154, 50);
        buttonAddMovie.TabIndex = 10;
        buttonAddMovie.Text = "Add movie";
        buttonAddMovie.UseVisualStyleBackColor = true;
        buttonAddMovie.Click += buttonAddMovie_Click;
        // 
        // buttonUpdateMovie
        // 
        buttonUpdateMovie.Enabled = false;
        buttonUpdateMovie.Location = new Point(711, 32);
        buttonUpdateMovie.Margin = new Padding(4, 5, 4, 5);
        buttonUpdateMovie.Name = "buttonUpdateMovie";
        buttonUpdateMovie.Size = new Size(154, 50);
        buttonUpdateMovie.TabIndex = 11;
        buttonUpdateMovie.Text = "Update movie";
        buttonUpdateMovie.UseVisualStyleBackColor = true;
        buttonUpdateMovie.Click += buttonUpdateMovie_Click;
        // 
        // buttonDeleteMovie
        // 
        buttonDeleteMovie.Enabled = false;
        buttonDeleteMovie.Location = new Point(940, 32);
        buttonDeleteMovie.Margin = new Padding(4, 5, 4, 5);
        buttonDeleteMovie.Name = "buttonDeleteMovie";
        buttonDeleteMovie.Size = new Size(154, 50);
        buttonDeleteMovie.TabIndex = 12;
        buttonDeleteMovie.Text = "Delete movie";
        buttonDeleteMovie.UseVisualStyleBackColor = true;
        buttonDeleteMovie.Click += buttonDeleteMovie_Click;
        // 
        // labelDirectors
        // 
        labelDirectors.Anchor = AnchorStyles.Bottom | AnchorStyles.Left | AnchorStyles.Right;
        labelDirectors.AutoSize = true;
        labelDirectors.Location = new Point(240, 292);
        labelDirectors.Margin = new Padding(4, 0, 4, 0);
        labelDirectors.Name = "labelDirectors";
        labelDirectors.Size = new Size(83, 25);
        labelDirectors.TabIndex = 13;
        labelDirectors.Text = "Directors";
        // 
        // labelMovies
        // 
        labelMovies.Anchor = AnchorStyles.Bottom | AnchorStyles.Right;
        labelMovies.AutoSize = true;
        labelMovies.Location = new Point(801, 125);
        labelMovies.Margin = new Padding(4, 0, 4, 0);
        labelMovies.Name = "labelMovies";
        labelMovies.Size = new Size(69, 25);
        labelMovies.TabIndex = 14;
        labelMovies.Text = "Movies";
        // 
        // dateTimePickerMovieLaunchDate
        // 
        dateTimePickerMovieLaunchDate.CustomFormat = "MMMM dd, yyyy";
        dateTimePickerMovieLaunchDate.Location = new Point(207, 149);
        dateTimePickerMovieLaunchDate.Name = "dateTimePickerMovieLaunchDate";
        dateTimePickerMovieLaunchDate.Size = new Size(231, 31);
        dateTimePickerMovieLaunchDate.TabIndex = 15;
        dateTimePickerMovieLaunchDate.Value = new DateTime(2024, 3, 15, 11, 10, 28, 0);
        // 
        // FormDirectorsMovies
        // 
        AutoScaleDimensions = new SizeF(10F, 25F);
        AutoScaleMode = AutoScaleMode.Font;
        ClientSize = new Size(1143, 750);
        Controls.Add(dateTimePickerMovieLaunchDate);
        Controls.Add(labelMovies);
        Controls.Add(labelDirectors);
        Controls.Add(buttonDeleteMovie);
        Controls.Add(buttonUpdateMovie);
        Controls.Add(buttonAddMovie);
        Controls.Add(textBoxMovieRunningTime);
        Controls.Add(labelMovieRunningTime);
        Controls.Add(labelMovieLaunchDate);
        Controls.Add(textBoxMovieDirectorID);
        Controls.Add(labelMovieDirectorID);
        Controls.Add(textBoxMovieTitle);
        Controls.Add(labelMovieTitle);
        Controls.Add(dataGridViewMovies);
        Controls.Add(dataGridViewDirectors);
        Margin = new Padding(4, 5, 4, 5);
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
    private Label labelMovieRunningTime;
    private TextBox textBoxMovieRunningTime;
    private Button buttonAddMovie;
    private Button buttonUpdateMovie;
    private Button buttonDeleteMovie;
    private Label labelDirectors;
    private Label labelMovies;
    private DateTimePicker dateTimePickerMovieLaunchDate;
}