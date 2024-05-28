using System.Data;
using System.Data.SqlClient;

namespace Briose;

public partial class FormCofetarie : Form {
    const string DbDataSource = "DESKTOP-0SVDHT0\\SQLEXPRESS";

    SqlConnection con = new SqlConnection("Data Source=" + DbDataSource +
                                         ";Initial Catalog=Briose;Integrated Security=True");

    SqlDataAdapter parentTableAdapter = new SqlDataAdapter();
    SqlDataAdapter childTableAdapter = new SqlDataAdapter();

    DataSet ds = new DataSet();

    readonly BindingSource parentBs = new BindingSource();
    readonly BindingSource childBs = new BindingSource();

    public FormCofetarie() {
        InitializeComponent();
    }

    public void ReloadContent() {
        parentTableAdapter.SelectCommand = new SqlCommand("SELECT * FROM Cofetarie", con);
        childTableAdapter.SelectCommand = new SqlCommand("SELECT * FROM Briose", con);

        ds.Clear();

        parentTableAdapter.Fill(ds, "Cofetarie");
        childTableAdapter.Fill(ds, "Briose");
    }

    public void ReloadChildContent() {
        childTableAdapter.SelectCommand = new SqlCommand("SELECT * FROM Briose", con);
        ds.Tables["Briose"]!.Clear();
        childTableAdapter.Fill(ds, "Briose");
    }

    public void ClearContent() {
        textBoxParentId.Text = "";
        textBoxCuloare.Text = "";
        textBoxMarime.Text = "";
        numericUpDownPret.Value = 0;
        numericUpDownPret.ResetText();
        textBoxParentId.Enabled = false;
        buttonUpdate.Enabled = true;
        buttonDelete.Enabled = true;
    }

    private void FormCofetarie_Load(object sender, EventArgs e) {
        try {
            ReloadContent();

            parentBs.DataSource = ds.Tables["Cofetarie"];
            dataGridViewParent.DataSource = parentBs;

            DataColumn parentPK = ds.Tables["Cofetarie"]!.Columns["id"]!;
            DataColumn childFK = ds.Tables["Briose"]!.Columns["id_cofetarie"]!;
            DataRelation relation = new DataRelation("fk_parent_child", parentPK, childFK);
            ds.Relations.Add(relation);


            childBs.DataSource = parentBs;
            childBs.DataMember = "fk_parent_child";
            dataGridViewChild.DataSource = childBs;
        }
        catch (Exception ex) {
            MessageBox.Show(ex.Message);
            con.Close();
        }
    }

    private void dataGridViewParent_CellClick(object sender, DataGridViewCellEventArgs e) {
        ClearContent();

        if (dataGridViewParent.SelectedCells.Count == 0 || e.RowIndex < 0) {
            return;
        }

        DataGridViewRow selectedRow = dataGridViewParent.Rows[e.RowIndex];
        int parentId = Convert.ToInt32(selectedRow.Cells["id"].Value);
        textBoxParentId.Text = parentId.ToString();

        childBs.Filter = $"id_cofetarie = {parentId}";
    }

    private void dataGridViewChild_CellClick(object sender, DataGridViewCellEventArgs e) {
        if (dataGridViewChild.SelectedCells.Count == 0 || e.RowIndex < 0) {
            ClearContent();
            return;
        }

        textBoxParentId.Enabled = true;
        buttonUpdate.Enabled = true;
        buttonDelete.Enabled = true;

        DataGridViewRow selectedRow = dataGridViewChild.Rows[e.RowIndex];

        textBoxCuloare.Text = Convert.ToString(selectedRow.Cells["culoare"].Value);
        textBoxParentId.Text = Convert.ToString(selectedRow.Cells["id_cofetarie"].Value);
        textBoxMarime.Text = Convert.ToString(selectedRow.Cells["marime"].Value);
        numericUpDownPret.Value = Convert.ToDecimal(selectedRow.Cells["pret"].Value);
    }

    private void buttonAdd_Click(object sender, EventArgs e) {
        if (dataGridViewParent.SelectedCells.Count <= 0) {
            MessageBox.Show("You must select a parent row!");
            return;
        }
        if (textBoxCuloare.Text.Count() == 0 ||
            textBoxMarime.Text.Count() == 0 ||
            textBoxParentId.Text.Count() == 0) {
            MessageBox.Show("Please fill in all the fields!");
            return;
        }
        int parentId = Convert.ToInt32(dataGridViewParent
            .SelectedCells[0]
            .OwningRow
            .Cells["id"]
            .Value);

        childTableAdapter.InsertCommand = new SqlCommand("insert into Briose " +
            "(id_cofetarie, marime, pret, culoare) values " +
            "(@id_cofetarie, @marime, @pret, @culoare)", con);

        childTableAdapter.InsertCommand.Parameters.Add("@id_cofetarie", SqlDbType.Int).Value =
            textBoxParentId.Text;
        childTableAdapter.InsertCommand.Parameters.Add("@marime", SqlDbType.Int).Value =
            textBoxMarime.Text;
        childTableAdapter.InsertCommand.Parameters.Add("@pret", SqlDbType.Float).Value =
            numericUpDownPret.Value;
        childTableAdapter.InsertCommand.Parameters.Add("@culoare", SqlDbType.VarChar).Value =
            textBoxCuloare.Text;

        try {
            con.Open();
            childTableAdapter.InsertCommand.ExecuteNonQuery();
            con.Close();

            ReloadChildContent();
        }
        catch (Exception ex) {
            MessageBox.Show(ex.Message);
            con.Close();
        }
    }

    private void buttonUpdate_Click(object sender, EventArgs e) {
        if (dataGridViewChild.SelectedCells.Count == 0) {
            MessageBox.Show("You must select a child row!");
            return;
        }
        if (textBoxCuloare.Text.Count() == 0 ||
            textBoxMarime.Text.Count() == 0 ||
            textBoxParentId.Text.Count() == 0) {
            MessageBox.Show("Please fill in all the fields!");
            return;
        }

        childTableAdapter.UpdateCommand = new SqlCommand("update Briose set " +
                       "marime = @marime, pret = @pret, culoare = @culoare, id_cofetarie = @id_cofetarie " +
                       "where id = @id", con);

        childTableAdapter.UpdateCommand.Parameters.Add("@id", SqlDbType.Int).Value =
            dataGridViewChild.SelectedCells[0].OwningRow.Cells["id"].Value;
        childTableAdapter.UpdateCommand.Parameters.Add("@id_cofetarie", SqlDbType.Int).Value =
            textBoxParentId.Text;
        childTableAdapter.UpdateCommand.Parameters.Add("@marime", SqlDbType.Int).Value =
            textBoxMarime.Text;
        childTableAdapter.UpdateCommand.Parameters.Add("@pret", SqlDbType.Float).Value =
            numericUpDownPret.Value;
        childTableAdapter.UpdateCommand.Parameters.Add("@culoare", SqlDbType.VarChar).Value =
            textBoxCuloare.Text;

        try {
            con.Open();
            int rowsAffected = childTableAdapter.UpdateCommand.ExecuteNonQuery();
            con.Close();

            if (rowsAffected == 0) {
                MessageBox.Show("No rows were updated!");
            }

            ReloadContent();
        }
        catch (Exception ex) {
            MessageBox.Show(ex.Message);
            con.Close();
        }
    }

    private void buttonDelete_Click(object sender, EventArgs e) {
        if (dataGridViewChild.SelectedCells.Count == 0) {
            MessageBox.Show("You must select a child row!");
            return;
        }

        childTableAdapter.DeleteCommand = new SqlCommand("delete from Briose where id = @id", con);

        childTableAdapter.DeleteCommand.Parameters.Add("@id", SqlDbType.Int).Value =
            dataGridViewChild.SelectedCells[0].OwningRow.Cells["id"].Value;

        try {
            con.Open();
            int rowsAffected = childTableAdapter.DeleteCommand.ExecuteNonQuery();
            con.Close();

            if (rowsAffected == 0) {
                MessageBox.Show("No rows were deleted!");
            }

            ReloadChildContent();
        }
        catch (Exception ex) {
            MessageBox.Show(ex.Message);
            con.Close();
        }
    }
}