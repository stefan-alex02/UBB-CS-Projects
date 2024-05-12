using System.Data;
using System.Data.SqlClient;
using System.IO;
using System.Runtime.CompilerServices;

namespace Laborator1;

public partial class FormDirectorsMovies : Form {
    // const string dbDataSource = "DESKTOP-HH5PFSL\\SQLEXPRESS";
    const string dbDataSource = "DESKTOP-0SVDHT0\\SQLEXPRESS";

    SqlConnection cs = new SqlConnection("Data Source=" + dbDataSource + 
        ";Initial Catalog=Cinematography;Integrated Security=True");

    SqlDataAdapter directorsAdapter = new SqlDataAdapter();
    SqlDataAdapter moviesAdapter = new SqlDataAdapter();

    DataSet ds = new DataSet();

    BindingSource directorsBS = new BindingSource();
    BindingSource moviesBS = new BindingSource();

    public FormDirectorsMovies() {
        InitializeComponent();
        dateTimePickerMovieLaunchDate.Format = DateTimePickerFormat.Custom;
    }

    private void ReloadContent() {
        directorsAdapter.SelectCommand = new SqlCommand("select * from Directors", cs);
        moviesAdapter.SelectCommand = new SqlCommand("select * from Movies", cs);

        ds.Clear();
        directorsAdapter.Fill(ds, "directors");
        moviesAdapter.Fill(ds, "movies");
    }

    private void Form1_Load(object sender, EventArgs e) {
        try {
            ReloadContent();

            directorsBS.DataSource = ds.Tables["directors"];
            dataGridViewDirectors.DataSource = directorsBS;

            DataColumn parentPK = ds.Tables["Directors"]!.Columns["DirectorID"]!;
            DataColumn childFK = ds.Tables["Movies"]!.Columns["DirectorID"]!;
            DataRelation relation = new DataRelation("fk_parent_child", parentPK, childFK);
            ds.Relations.Add(relation);


            moviesBS.DataSource = directorsBS;
            moviesBS.DataMember = "fk_parent_child";
            dataGridViewMovies.DataSource = moviesBS;
        }
        catch (Exception ex) {
            MessageBox.Show(ex.Message);
            cs.Close();
        }
    }

    public void ClearTextBoxes() {
        textBoxMovieTitle.Text = String.Empty;
        textBoxMovieDirectorID.Text = String.Empty;
        dateTimePickerMovieLaunchDate.Value = DateTime.Today;
        textBoxMovieRunningTime.Text = String.Empty;

        textBoxMovieDirectorID.Enabled = false;
        buttonUpdateMovie.Enabled = false;
        buttonDeleteMovie.Enabled = false;
    }

    private void dataGridViewDirectors_CellClick(object sender, DataGridViewCellEventArgs e) {
        ClearTextBoxes();

        if (dataGridViewDirectors.SelectedCells.Count == 0 || e.RowIndex < 0) {
            return;
        }

        DataGridViewRow selectedRow = dataGridViewDirectors.Rows[e.RowIndex];
        int directorID = Convert.ToInt32(selectedRow.Cells["DirectorID"].Value);

        moviesBS.Filter = $"DirectorID = {directorID}";
    }

    private void dataGridViewMovies_CellClick(object sender, DataGridViewCellEventArgs e) {
        if (dataGridViewMovies.SelectedCells.Count == 0 || e.RowIndex < 0) {
            ClearTextBoxes();
            return;
        }

        textBoxMovieDirectorID.Enabled = true;
        buttonUpdateMovie.Enabled = true;
        buttonDeleteMovie.Enabled = true;

        DataGridViewRow selectedRow = dataGridViewMovies.Rows[e.RowIndex];

        textBoxMovieTitle.Text = Convert.ToString(selectedRow.Cells["Title"].Value);
        textBoxMovieDirectorID.Text = Convert.ToString(selectedRow.Cells["DirectorID"].Value);
        dateTimePickerMovieLaunchDate.Value = Convert.ToDateTime(selectedRow.Cells["LaunchDate"].Value);
        textBoxMovieRunningTime.Text = Convert.ToString(selectedRow.Cells["RunningTime"].Value);
    }

    private void buttonAddMovie_Click(object sender, EventArgs e) {
        if (dataGridViewDirectors.SelectedCells.Count <= 0) {
            MessageBox.Show("You must select a director!");
            return;
        }

        if (textBoxMovieTitle.Text.Count() == 0) {
            MessageBox.Show("You must write a title!");
            return;
        }

        if (textBoxMovieRunningTime.Text.Count() == 0) {
            MessageBox.Show("You must write a remaining time!");
            return;
        }

        int directorID = Convert.ToInt32(dataGridViewDirectors
            .SelectedCells[0]
            .OwningRow
            .Cells["DirectorID"]
            .Value);

        moviesAdapter.InsertCommand = new SqlCommand("insert into Movies values " +
            "(@title, @directorID, @launchDate, @runningTime)", cs);

        moviesAdapter.InsertCommand.Parameters.Add("@title", SqlDbType.VarChar).Value = 
            textBoxMovieTitle.Text;
        moviesAdapter.InsertCommand.Parameters.Add("@directorID", SqlDbType.Int).Value =
            directorID;
        moviesAdapter.InsertCommand.Parameters.Add("@launchDate", SqlDbType.Date).Value = 
            dateTimePickerMovieLaunchDate.Value;
        moviesAdapter.InsertCommand.Parameters.Add("@runningTime", SqlDbType.Int).Value = 
            textBoxMovieRunningTime.Text;

        try {
            cs.Open();
            moviesAdapter.InsertCommand.ExecuteNonQuery();
            cs.Close();

            ReloadContent();
        }
        catch (Exception ex) {
            MessageBox.Show(ex.Message);
            cs.Close();
        }
    }

    private void buttonUpdateMovie_Click(object sender, EventArgs e) {
        if (dataGridViewMovies.SelectedCells.Count < 1) {
            MessageBox.Show("You must select a record to update!");
            return;
        }

        if (textBoxMovieTitle.Text.Count() == 0) {
            MessageBox.Show("You must write a title!");
            return;
        }

        if (textBoxMovieRunningTime.Text.Count() == 0) {
            MessageBox.Show("You must write a remaining time!");
            return;
        }

        int movieID = Convert.ToInt32(dataGridViewMovies
            .SelectedCells[0]
            .OwningRow
            .Cells["MovieID"]
            .Value);

        moviesAdapter.UpdateCommand = new SqlCommand("update Movies set " +
            "Title = @title, DirectorID = @directorID, " +
            "LaunchDate = @launchDate, RunningTime = @runningTime " +
            "where MovieID = @movieID", cs);

        moviesAdapter.UpdateCommand.Parameters.Add("@title", SqlDbType.VarChar).Value =
            textBoxMovieTitle.Text;
        moviesAdapter.UpdateCommand.Parameters.Add("@directorID", SqlDbType.Int).Value =
            textBoxMovieDirectorID.Text;
        moviesAdapter.UpdateCommand.Parameters.Add("@launchDate", SqlDbType.Date).Value =
            dateTimePickerMovieLaunchDate.Value;
        moviesAdapter.UpdateCommand.Parameters.Add("@runningTime", SqlDbType.Int).Value =
            textBoxMovieRunningTime.Text;

        moviesAdapter.UpdateCommand.Parameters.Add("@movieID", SqlDbType.Int).Value =
            movieID;

        try {
            cs.Open();
            int result = moviesAdapter.UpdateCommand.ExecuteNonQuery();
            cs.Close();

            if (result < 1) {
                MessageBox.Show("Failed to update movie record");
            }

            ReloadContent();
        }
        catch (Exception ex) {
            MessageBox.Show(ex.Message);
            cs.Close();
        }
    }

    private void buttonDeleteMovie_Click(object sender, EventArgs e) {
        if (dataGridViewMovies.SelectedCells.Count < 1) {
            MessageBox.Show("You must select a record to delete!");
            return;
        }

        int movieID = Convert.ToInt32(dataGridViewMovies.SelectedCells[0].OwningRow.Cells["MovieID"].Value);


        if (MessageBox.Show("Are you sure you want to delete?",
            "Confirm Deletion",
            MessageBoxButtons.YesNo) == DialogResult.No) {
            return;
        }

        moviesAdapter.DeleteCommand = new SqlCommand("delete from Movies where MovieID = @movieID", cs);

        moviesAdapter.DeleteCommand.Parameters.Add("@movieID", SqlDbType.Int).Value = movieID;

        try {
            cs.Open();
            int result = moviesAdapter.DeleteCommand.ExecuteNonQuery();
            cs.Close();

            ReloadContent();

            dataGridViewMovies.ClearSelection();
        }
        catch (Exception ex) {
            MessageBox.Show(ex.Message);
            cs.Close();
        }
    }
}