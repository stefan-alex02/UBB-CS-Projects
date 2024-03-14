using System.Data;
using System.Data.SqlClient;
using System.Runtime.CompilerServices;

namespace Laborator1;

public partial class FormDirectorsMovies : Form {
    SqlConnection cs = new SqlConnection("Data Source=DESKTOP-HH5PFSL\\SQLEXPRESS;Initial Catalog=Cinematography;Integrated Security=True");

    SqlDataAdapter directorsAdapter = new SqlDataAdapter();
    SqlDataAdapter moviesAdapter = new SqlDataAdapter();

    DataSet ds = new DataSet();

    BindingSource directorsBS = new BindingSource();
    BindingSource moviesBS = new BindingSource();

    public FormDirectorsMovies() {
        InitializeComponent();
    }

    private void Form1_Load(object sender, EventArgs e) {
        try {
            directorsAdapter.SelectCommand = new SqlCommand("select * from Directors", cs);
            moviesAdapter.SelectCommand = new SqlCommand("select * from Movies", cs);

            ds.Clear();
            directorsAdapter.Fill(ds, "directors");
            moviesAdapter.Fill(ds, "movies");

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

    private void dataGridViewDirectors_CellMouseClick(object sender, DataGridViewCellMouseEventArgs e) {
        DataGridViewRow selectedRow = dataGridViewDirectors.Rows[e.RowIndex];
        int directorID = Convert.ToInt32(selectedRow.Cells["DirectorID"].Value);

        moviesBS.Filter = $"DirectorID = {directorID}";

        textBoxMovieTitle.Text = String.Empty;
        textBoxMovieDirectorID.Text = String.Empty;
        textBoxMovieLaunchDate.Text = String.Empty;
        textBoxMovieRunningTime.Text = String.Empty;
    }

    private void dataGridViewMovies_CellClick(object sender, DataGridViewCellEventArgs e) {
        DataGridViewRow selectedRow = dataGridViewMovies.Rows[e.RowIndex];

        textBoxMovieTitle.Text = Convert.ToString(selectedRow.Cells["Title"].Value);
        textBoxMovieDirectorID.Text = Convert.ToString(selectedRow.Cells["DirectorID"].Value);
        textBoxMovieLaunchDate.Text = Convert.ToString(selectedRow.Cells["LaunchDate"].Value);
        textBoxMovieRunningTime.Text = Convert.ToString(selectedRow.Cells["RunningTime"].Value);
    }
}