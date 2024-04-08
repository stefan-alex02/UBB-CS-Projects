using System;
using System.Drawing;
using System.Windows.Forms;

namespace Laborator2 {
    partial class FormTables {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
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
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent() {
            this.dataGridViewParentTable = new System.Windows.Forms.DataGridView();
            this.dataGridViewChild = new System.Windows.Forms.DataGridView();
            this.buttonAddMovie = new System.Windows.Forms.Button();
            this.buttonUpdateMovie = new System.Windows.Forms.Button();
            this.buttonDeleteMovie = new System.Windows.Forms.Button();
            this.labelParentTable = new System.Windows.Forms.Label();
            this.labelChildTable = new System.Windows.Forms.Label();
            this.panelTextBoxes = new System.Windows.Forms.Panel();
            ((System.ComponentModel.ISupportInitialize)(this.dataGridViewParentTable)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.dataGridViewChild)).BeginInit();
            this.SuspendLayout();
            // 
            // dataGridViewParentTable
            // 
            this.dataGridViewParentTable.AllowUserToAddRows = false;
            this.dataGridViewParentTable.AllowUserToDeleteRows = false;
            this.dataGridViewParentTable.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)));
            this.dataGridViewParentTable.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.dataGridViewParentTable.EditMode = System.Windows.Forms.DataGridViewEditMode.EditProgrammatically;
            this.dataGridViewParentTable.Location = new System.Drawing.Point(20, 181);
            this.dataGridViewParentTable.Margin = new System.Windows.Forms.Padding(2, 3, 2, 3);
            this.dataGridViewParentTable.MaximumSize = new System.Drawing.Size(400, 208);
            this.dataGridViewParentTable.MinimumSize = new System.Drawing.Size(120, 182);
            this.dataGridViewParentTable.MultiSelect = false;
            this.dataGridViewParentTable.Name = "dataGridViewParentTable";
            this.dataGridViewParentTable.RowHeadersWidth = 62;
            this.dataGridViewParentTable.RowTemplate.Height = 25;
            this.dataGridViewParentTable.Size = new System.Drawing.Size(325, 182);
            this.dataGridViewParentTable.TabIndex = 0;
            this.dataGridViewParentTable.CellClick += new System.Windows.Forms.DataGridViewCellEventHandler(this.dataGridViewParent_CellClick);
            // 
            // dataGridViewChild
            // 
            this.dataGridViewChild.AllowUserToAddRows = false;
            this.dataGridViewChild.AllowUserToDeleteRows = false;
            this.dataGridViewChild.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.dataGridViewChild.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.dataGridViewChild.EditMode = System.Windows.Forms.DataGridViewEditMode.EditProgrammatically;
            this.dataGridViewChild.Location = new System.Drawing.Point(369, 81);
            this.dataGridViewChild.Margin = new System.Windows.Forms.Padding(2, 3, 2, 3);
            this.dataGridViewChild.MultiSelect = false;
            this.dataGridViewChild.Name = "dataGridViewChild";
            this.dataGridViewChild.RowHeadersWidth = 62;
            this.dataGridViewChild.RowTemplate.Height = 25;
            this.dataGridViewChild.Size = new System.Drawing.Size(309, 282);
            this.dataGridViewChild.TabIndex = 1;
            this.dataGridViewChild.CellClick += new System.Windows.Forms.DataGridViewCellEventHandler(this.dataGridViewMovies_CellClick);
            // 
            // buttonAddMovie
            // 
            this.buttonAddMovie.Location = new System.Drawing.Point(369, 17);
            this.buttonAddMovie.Margin = new System.Windows.Forms.Padding(2, 3, 2, 3);
            this.buttonAddMovie.Name = "buttonAddMovie";
            this.buttonAddMovie.Size = new System.Drawing.Size(92, 26);
            this.buttonAddMovie.TabIndex = 10;
            this.buttonAddMovie.Text = "Add record";
            this.buttonAddMovie.UseVisualStyleBackColor = true;
            this.buttonAddMovie.Click += new System.EventHandler(this.buttonAddChild_Click);
            // 
            // buttonUpdateMovie
            // 
            this.buttonUpdateMovie.Location = new System.Drawing.Point(476, 17);
            this.buttonUpdateMovie.Margin = new System.Windows.Forms.Padding(2, 3, 2, 3);
            this.buttonUpdateMovie.Name = "buttonUpdateMovie";
            this.buttonUpdateMovie.Size = new System.Drawing.Size(92, 26);
            this.buttonUpdateMovie.TabIndex = 11;
            this.buttonUpdateMovie.Text = "Update record";
            this.buttonUpdateMovie.UseVisualStyleBackColor = true;
            this.buttonUpdateMovie.Click += new System.EventHandler(this.buttonUpdateChild_Click);
            // 
            // buttonDeleteMovie
            // 
            this.buttonDeleteMovie.Location = new System.Drawing.Point(586, 17);
            this.buttonDeleteMovie.Margin = new System.Windows.Forms.Padding(2, 3, 2, 3);
            this.buttonDeleteMovie.Name = "buttonDeleteMovie";
            this.buttonDeleteMovie.Size = new System.Drawing.Size(92, 26);
            this.buttonDeleteMovie.TabIndex = 12;
            this.buttonDeleteMovie.Text = "Delete record";
            this.buttonDeleteMovie.UseVisualStyleBackColor = true;
            this.buttonDeleteMovie.Click += new System.EventHandler(this.buttonDeleteChild_Click);
            // 
            // labelParentTable
            // 
            this.labelParentTable.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.labelParentTable.AutoSize = true;
            this.labelParentTable.Location = new System.Drawing.Point(137, 165);
            this.labelParentTable.Margin = new System.Windows.Forms.Padding(2, 0, 2, 0);
            this.labelParentTable.Name = "labelParentTable";
            this.labelParentTable.Size = new System.Drawing.Size(74, 13);
            this.labelParentTable.TabIndex = 13;
            this.labelParentTable.Text = "[Parent Table]";
            // 
            // labelChildTable
            // 
            this.labelChildTable.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
            this.labelChildTable.AutoSize = true;
            this.labelChildTable.Location = new System.Drawing.Point(502, 65);
            this.labelChildTable.Margin = new System.Windows.Forms.Padding(2, 0, 2, 0);
            this.labelChildTable.Name = "labelChildTable";
            this.labelChildTable.Size = new System.Drawing.Size(66, 13);
            this.labelChildTable.TabIndex = 14;
            this.labelChildTable.Text = "[Child Table]";
            // 
            // panelTextBoxes
            // 
            this.panelTextBoxes.Location = new System.Drawing.Point(36, 21);
            this.panelTextBoxes.Name = "panelTextBoxes";
            this.panelTextBoxes.Size = new System.Drawing.Size(274, 116);
            this.panelTextBoxes.TabIndex = 15;
            // 
            // FormTables
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(689, 390);
            this.Controls.Add(this.panelTextBoxes);
            this.Controls.Add(this.labelChildTable);
            this.Controls.Add(this.labelParentTable);
            this.Controls.Add(this.buttonDeleteMovie);
            this.Controls.Add(this.buttonUpdateMovie);
            this.Controls.Add(this.buttonAddMovie);
            this.Controls.Add(this.dataGridViewChild);
            this.Controls.Add(this.dataGridViewParentTable);
            this.Margin = new System.Windows.Forms.Padding(2, 3, 2, 3);
            this.Name = "FormTables";
            this.Text = "Parent & Child";
            this.Load += new System.EventHandler(this.FormTables_Load);
            ((System.ComponentModel.ISupportInitialize)(this.dataGridViewParentTable)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.dataGridViewChild)).EndInit();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private DataGridView dataGridViewParentTable;
        private DataGridView dataGridViewChild;
        private Button buttonAddMovie;
        private Button buttonUpdateMovie;
        private Button buttonDeleteMovie;
        private Label labelParentTable;
        private Label labelChildTable;
        private Panel panelTextBoxes;
    }
}