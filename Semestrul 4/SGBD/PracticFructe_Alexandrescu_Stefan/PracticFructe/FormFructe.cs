using System.Data;
using System.Data.SqlClient;

namespace PracticFructe {
    public partial class FormFructe : Form {
        const string DbDataSource = "DESKTOP-0SVDHT0\\SQLEXPRESS";

        SqlConnection con = new SqlConnection("Data Source=" + DbDataSource +
                                             ";Initial Catalog=S10;Integrated Security=True");

        SqlDataAdapter parentTableAdapter = new SqlDataAdapter();
        SqlDataAdapter childTableAdapter = new SqlDataAdapter();

        DataSet ds = new DataSet();

        readonly BindingSource parentBs = new BindingSource();
        readonly BindingSource childBs = new BindingSource();

        public FormFructe() {
            InitializeComponent();
        }

        public void ReloadContent() {
            parentTableAdapter.SelectCommand = new SqlCommand("SELECT * FROM TipuriFructe", con);
            childTableAdapter.SelectCommand = new SqlCommand("SELECT * FROM Fructe", con);

            ds.Clear();

            parentTableAdapter.Fill(ds, "TipuriFructe");
            childTableAdapter.Fill(ds, "Fructe");
        }

        public void ReloadChildContent() {
            childTableAdapter.SelectCommand = new SqlCommand("SELECT * FROM Fructe", con);
            ds.Tables["Fructe"]!.Clear();
            childTableAdapter.Fill(ds, "Fructe");
        }

        public void ClearContent() {
            textBoxParentId.Text = "";
            textBoxDenumire.Text = "";
            textBoxCuloare.Text = "";
            textBoxLunaOptima.Text = "";
            numericUpDownPret.Value = 0;
            numericUpDownPret.ResetText();

            textBoxParentId.Enabled = false;
            buttonUpdate.Enabled = true;
            buttonDelete.Enabled = true;
        }

        private void FormFructe_Load(object sender, EventArgs e) {
            try {
                ReloadContent();

                parentBs.DataSource = ds.Tables["TipuriFructe"];
                dataGridViewParent.DataSource = parentBs;

                DataColumn parentPK = ds.Tables["TipuriFructe"]!.Columns["Tid"]!;
                DataColumn childFK = ds.Tables["Fructe"]!.Columns["Tid"]!;
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
            int parentId = Convert.ToInt32(selectedRow.Cells["Tid"].Value);
            textBoxParentId.Text = parentId.ToString();

            childBs.Filter = $"Tid = {parentId}";
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

            textBoxParentId.Text = Convert.ToString(selectedRow.Cells["Tid"].Value);
            textBoxDenumire.Text = Convert.ToString(selectedRow.Cells["Denumire"].Value);
            textBoxCuloare.Text = Convert.ToString(selectedRow.Cells["Culoare"].Value);
            textBoxLunaOptima.Text = Convert.ToString(selectedRow.Cells["LunaOptimaCulegere"].Value);
            numericUpDownPret.Value = Convert.ToDecimal(selectedRow.Cells["PretMediu"].Value);
        }

        private void buttonAdd_Click(object sender, EventArgs e) {
            if (dataGridViewParent.SelectedCells.Count <= 0) {
                MessageBox.Show("You must select a fruit type row!");
                return;
            }
            if (textBoxDenumire.Text.Count() == 0 ||
                textBoxCuloare.Text.Count() == 0 ||
                textBoxLunaOptima.Text.Count() == 0 ||
                textBoxParentId.Text.Count() == 0) {
                MessageBox.Show("Please fill in all the fields!");
                return;
            }
            //int parentId = Convert.ToInt32(dataGridViewParent
            //    .SelectedCells[0]
            //    .OwningRow
            //    .Cells["id"]
            //    .Value);

            childTableAdapter.InsertCommand = new SqlCommand("insert into Fructe " +
                "(Tid, Denumire, Culoare, LunaOptimaCulegere, PretMediu) values " +
                "(@Tid, @Denumire, @Culoare, @LunaOptimaCulegere, @PretMediu)", con);

            childTableAdapter.InsertCommand.Parameters.Add("@Tid", SqlDbType.Int).Value =
                textBoxParentId.Text;
            childTableAdapter.InsertCommand.Parameters.Add("@Denumire", SqlDbType.VarChar).Value =
                textBoxDenumire.Text;
            childTableAdapter.InsertCommand.Parameters.Add("@Culoare", SqlDbType.VarChar).Value =
                textBoxCuloare.Text;
            childTableAdapter.InsertCommand.Parameters.Add("@LunaOptimaCulegere", SqlDbType.VarChar).Value =
                textBoxLunaOptima.Text;
            childTableAdapter.InsertCommand.Parameters.Add("@PretMediu", SqlDbType.Int).Value =
                numericUpDownPret.Value;

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
                MessageBox.Show("You must select a fruit!");
                return;
            }
            if (textBoxDenumire.Text.Count() == 0 ||
                textBoxCuloare.Text.Count() == 0 ||
                textBoxLunaOptima.Text.Count() == 0 ||
                textBoxParentId.Text.Count() == 0) {
                MessageBox.Show("Please fill in all the fields!");
                return;
            }

            childTableAdapter.UpdateCommand = new SqlCommand("update Fructe set " +
                           "Denumire = @Denumire, Culoare = @Culoare, " +
                           "LunaOptimaCulegere = @LunaOptimaCulegere, PretMediu = @PretMediu, " +
                           "Tid = @Tid " +
                           "where Fid = @Fid", con);

            childTableAdapter.UpdateCommand.Parameters.Add("@Fid", SqlDbType.Int).Value =
                dataGridViewChild.SelectedCells[0].OwningRow.Cells["Fid"].Value;

            childTableAdapter.UpdateCommand.Parameters.Add("@Tid", SqlDbType.Int).Value =
                textBoxParentId.Text;
            childTableAdapter.UpdateCommand.Parameters.Add("@Denumire", SqlDbType.VarChar).Value =
                textBoxDenumire.Text;
            childTableAdapter.UpdateCommand.Parameters.Add("@Culoare", SqlDbType.VarChar).Value =
                textBoxCuloare.Text;
            childTableAdapter.UpdateCommand.Parameters.Add("@LunaOptimaCulegere", SqlDbType.VarChar).Value =
                textBoxLunaOptima.Text;
            childTableAdapter.UpdateCommand.Parameters.Add("@PretMediu", SqlDbType.Int).Value =
                numericUpDownPret.Value;

            try {
                con.Open();
                int rowsAffected = childTableAdapter.UpdateCommand.ExecuteNonQuery();
                con.Close();

                if (rowsAffected == 0) {
                    MessageBox.Show("No rows were updated!");
                }

                ReloadChildContent();
            }
            catch (Exception ex) {
                MessageBox.Show(ex.Message);
                con.Close();
            }
        }

        private void buttonDelete_Click(object sender, EventArgs e) {
            if (dataGridViewChild.SelectedCells.Count == 0) {
                MessageBox.Show("You must select a fruit row!");
                return;
            }

            childTableAdapter.DeleteCommand = new SqlCommand("delete from Fructe where Fid = @Fid", con);

            childTableAdapter.DeleteCommand.Parameters.Add("@Fid", SqlDbType.Int).Value =
                dataGridViewChild.SelectedCells[0].OwningRow.Cells["Fid"].Value;

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
}
