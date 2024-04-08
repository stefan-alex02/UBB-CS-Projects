using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Configuration;
using System.Data;
using System.Data.SqlClient;
using System.Drawing;
using System.Linq;
using System.Security.Cryptography;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Laborator2 {
    public partial class FormTables : Form {
        private static string connectionString = ConfigurationManager.ConnectionStrings["cs"].ConnectionString;
        private static SqlConnection cs;

        private static SqlDataAdapter parentAdapter = new SqlDataAdapter();
        private static SqlDataAdapter childAdapter = new SqlDataAdapter();

        private static DataSet ds = new DataSet();

        private static BindingSource parentBS = new BindingSource();
        private static BindingSource childBS = new BindingSource();

        private static string ParentTableName = ConfigurationManager.AppSettings["ParentTableName"];
        private static string ChildTableName = ConfigurationManager.AppSettings["ChildTableName"];
        
        private static string ChildColumnNames = ConfigurationManager.AppSettings["ChildColumnNames"];

        private static string ParentPrimaryKey = ConfigurationManager.AppSettings.Get("ParentPrimaryKey");
        private static string ChildForeignKey = ConfigurationManager.AppSettings.Get("ChildForeignKey");
        private static string ChildPrimaryKeys = ConfigurationManager.AppSettings.Get("ChildPrimaryKeys");

        private List<string> ChildColumnNamesList;
        private static List<string> ChildPrimaryKeysList;

        public FormTables() {
            InitializeComponent();
            this.Text = ParentTableName + " - " + ChildTableName;
            labelParentTable.Text = ParentTableName;
            labelChildTable.Text = ChildTableName;

            cs = new SqlConnection(connectionString);

            ChildColumnNamesList = new List<string>(ChildColumnNames.Split(','));
            ChildPrimaryKeysList = new List<string>(ChildPrimaryKeys.Split(','));

            foreach (string column in ChildColumnNamesList) {
                TextBox textBox = new TextBox();
                textBox.Name = column;
                textBox.Location = new Point(100, 10 + panelTextBoxes.Controls.Count / 2 * 30);

                panelTextBoxes.Controls.Add(textBox);

                Label label = new Label();
                label.Text = column;
                label.Location = new Point(0, 10 + panelTextBoxes.Controls.Count / 2 * 30);

                panelTextBoxes.Controls.Add(label);
            }

            panelTextBoxes.Location = new Point(30, 10);
            panelTextBoxes.Height = 20 + panelTextBoxes.Controls.Count / 2 * 30;
            panelTextBoxes.Anchor = AnchorStyles.Top | AnchorStyles.Left;


            childAdapter.InsertCommand = new SqlCommand("insert into " + ChildTableName + " (" + ChildColumnNames + 
                ") values (@" + string.Join(", @", ChildColumnNamesList) + ")", cs);

            childAdapter.UpdateCommand = new SqlCommand("update " + ChildTableName + " set " + 
                               string.Join(", ", ChildColumnNamesList.Select(col => col + " = @" + col)) + 
                               " where " + string.Join(" and ", ChildPrimaryKeysList.Select(key => key + " = @" + key)), 
                               cs);

            childAdapter.DeleteCommand = new SqlCommand("delete from " + ChildTableName + 
                    " where " + string.Join(" and ", ChildPrimaryKeysList.Select(key => key + " = @" + key)), 
                    cs);
        }
        
        private void Populate() {
            parentAdapter.SelectCommand = new SqlCommand("select * from " + ParentTableName, cs);
            childAdapter.SelectCommand = new SqlCommand("select * from " + ChildTableName, cs);

            ds.Clear();
            parentAdapter.Fill(ds, ParentTableName);
            childAdapter.Fill(ds, ChildTableName);
        }

        private void Repopulate() {
            ds.Tables[ChildTableName].Clear();
            childAdapter.Fill(ds, ChildTableName);
        }

        private void FormTables_Load(object sender, EventArgs e) {
            try {
                Populate();

                parentBS.DataSource = ds.Tables[ParentTableName];
                dataGridViewParentTable.DataSource = parentBS;

                DataColumn parentPK = ds.Tables[ParentTableName]!.Columns[ParentPrimaryKey]!;
                DataColumn childFK = ds.Tables[ChildTableName]!.Columns[ChildForeignKey]!;
                DataRelation relation = new DataRelation("fk_parent_child", parentPK, childFK);
                ds.Relations.Add(relation);


                childBS.DataSource = parentBS;
                childBS.DataMember = "fk_parent_child";
                dataGridViewChild.DataSource = childBS;
            }
            catch (Exception ex) {
                MessageBox.Show(ex.Message);
                Console.Error.WriteLine(ex.StackTrace);
                cs.Close();
            }
        }

        public void ClearTextBoxes() {
            foreach (Control control in panelTextBoxes.Controls) {
                if (control is TextBox) {
                    control.Text = "";
                }
            }
        }

        private void dataGridViewParent_CellClick(object sender, DataGridViewCellEventArgs e) {
            ClearTextBoxes();

            if (dataGridViewParentTable.SelectedCells.Count == 0 || e.RowIndex < 0) {
                return;
            }

            DataGridViewRow selectedRow = dataGridViewParentTable.Rows[e.RowIndex];
            int parentID = Convert.ToInt32(selectedRow.Cells[ParentPrimaryKey].Value);

            childBS.Filter = ChildForeignKey + $" = {parentID}";
        }

        private void dataGridViewMovies_CellClick(object sender, DataGridViewCellEventArgs e) {
            if (dataGridViewChild.SelectedCells.Count == 0 || e.RowIndex < 0) {
                ClearTextBoxes();
                return;
            }

            DataGridViewRow selectedRow = dataGridViewChild.Rows[e.RowIndex];

            foreach (string column in ChildColumnNamesList) {
                TextBox textBox = (TextBox)panelTextBoxes.Controls[column];
                textBox.Text = selectedRow.Cells[column].Value.ToString();
            }
        }

        private void buttonAddChild_Click(object sender, EventArgs e) {
            try {
                childAdapter.InsertCommand.Parameters.Clear();

                if (dataGridViewParentTable.SelectedCells.Count <= 0) {
                    MessageBox.Show("You must select a director!");
                    return;
                }

                foreach (string column in ChildColumnNamesList) {
                    TextBox textBox = (TextBox)panelTextBoxes.Controls[column];

                    if (textBox.Text.Count() == 0) {
                        MessageBox.Show("You must fill all the fields!");
                        return;
                    }

                    childAdapter.InsertCommand.Parameters.AddWithValue("@" + column, textBox.Text);
                }

                int directorID = Convert.ToInt32(dataGridViewParentTable
                    .SelectedCells[0]
                    .OwningRow
                    .Cells[ParentPrimaryKey]
                    .Value);

                cs.Open();
                childAdapter.InsertCommand.ExecuteNonQuery();
                cs.Close();

                Repopulate();
            }
            catch (Exception ex) {
                MessageBox.Show(ex.Message);
                cs.Close();
            }
        }

        private void buttonUpdateChild_Click(object sender, EventArgs e) {
            try {
                childAdapter.UpdateCommand.Parameters.Clear();

                if (dataGridViewChild.SelectedCells.Count < 1) {
                    MessageBox.Show("You must select a record to update!");
                    return;
                }

                foreach (string column in ChildColumnNamesList) {
                    TextBox textBox = (TextBox)panelTextBoxes.Controls[column];

                    if (textBox.Text.Count() == 0) {
                        MessageBox.Show("You must fill all the fields!");
                        return;
                    }

                    childAdapter.UpdateCommand.Parameters.AddWithValue("@" + column, textBox.Text);
                }

                foreach (string key in ChildPrimaryKeysList) {
                    childAdapter.UpdateCommand.Parameters.AddWithValue("@" + key, dataGridViewChild
                        .SelectedCells[0]
                        .OwningRow
                        .Cells[key]
                        .Value);
                }

                cs.Open();
                int result = childAdapter.UpdateCommand.ExecuteNonQuery();
                cs.Close();

                if (result < 1) {
                    MessageBox.Show("Failed to update movie record");
                }

                Repopulate();
            }
            catch (Exception ex) {
                MessageBox.Show(ex.Message);
                cs.Close();
            }
        }

        private void buttonDeleteChild_Click(object sender, EventArgs e) {
            try {
                childAdapter.DeleteCommand.Parameters.Clear();

                if (dataGridViewChild.SelectedCells.Count < 1) {
                    MessageBox.Show("You must select a record to delete!");
                    return;
                }
                if (MessageBox.Show("Are you sure you want to delete?",
                    "Confirm Deletion",
                    MessageBoxButtons.YesNo) == DialogResult.No) {
                    return;
                }

                foreach (string key in ChildPrimaryKeysList) {
                    childAdapter.DeleteCommand.Parameters.AddWithValue("@" + key, dataGridViewChild
                        .SelectedCells[0]
                        .OwningRow
                        .Cells[key]
                        .Value);
                }

                cs.Open();
                int result = childAdapter.DeleteCommand.ExecuteNonQuery();
                cs.Close();

                Repopulate();

                dataGridViewChild.ClearSelection();
            }
            catch (Exception ex) {
                MessageBox.Show(ex.Message);
                cs.Close();
            }
        }
    }
}