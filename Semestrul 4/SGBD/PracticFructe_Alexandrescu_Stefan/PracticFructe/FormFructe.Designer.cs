namespace PracticFructe {
    partial class FormFructe {
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
            dataGridViewParent = new DataGridView();
            dataGridViewChild = new DataGridView();
            labelChild = new Label();
            labelParent = new Label();
            numericUpDownPret = new NumericUpDown();
            textBoxDenumire = new TextBox();
            textBoxCuloare = new TextBox();
            textBoxParentId = new TextBox();
            buttonAdd = new Button();
            buttonUpdate = new Button();
            buttonDelete = new Button();
            textBoxLunaOptima = new TextBox();
            labelPretMediu = new Label();
            ((System.ComponentModel.ISupportInitialize)dataGridViewParent).BeginInit();
            ((System.ComponentModel.ISupportInitialize)dataGridViewChild).BeginInit();
            ((System.ComponentModel.ISupportInitialize)numericUpDownPret).BeginInit();
            SuspendLayout();
            // 
            // dataGridViewParent
            // 
            dataGridViewParent.AllowUserToAddRows = false;
            dataGridViewParent.AllowUserToDeleteRows = false;
            dataGridViewParent.AllowUserToOrderColumns = true;
            dataGridViewParent.Anchor = AnchorStyles.Bottom | AnchorStyles.Left;
            dataGridViewParent.ColumnHeadersHeightSizeMode = DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            dataGridViewParent.EditMode = DataGridViewEditMode.EditProgrammatically;
            dataGridViewParent.Location = new Point(49, 293);
            dataGridViewParent.Name = "dataGridViewParent";
            dataGridViewParent.RowHeadersWidth = 62;
            dataGridViewParent.SelectionMode = DataGridViewSelectionMode.FullRowSelect;
            dataGridViewParent.Size = new Size(388, 225);
            dataGridViewParent.TabIndex = 0;
            dataGridViewParent.CellClick += dataGridViewParent_CellClick;
            // 
            // dataGridViewChild
            // 
            dataGridViewChild.AllowUserToAddRows = false;
            dataGridViewChild.AllowUserToDeleteRows = false;
            dataGridViewChild.AllowUserToOrderColumns = true;
            dataGridViewChild.Anchor = AnchorStyles.Top | AnchorStyles.Bottom | AnchorStyles.Left | AnchorStyles.Right;
            dataGridViewChild.ColumnHeadersHeightSizeMode = DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            dataGridViewChild.EditMode = DataGridViewEditMode.EditProgrammatically;
            dataGridViewChild.Location = new Point(466, 90);
            dataGridViewChild.Name = "dataGridViewChild";
            dataGridViewChild.RowHeadersWidth = 62;
            dataGridViewChild.SelectionMode = DataGridViewSelectionMode.FullRowSelect;
            dataGridViewChild.Size = new Size(595, 428);
            dataGridViewChild.TabIndex = 1;
            dataGridViewChild.CellClick += dataGridViewChild_CellClick;
            // 
            // labelChild
            // 
            labelChild.Anchor = AnchorStyles.Top | AnchorStyles.Bottom | AnchorStyles.Left | AnchorStyles.Right;
            labelChild.AutoSize = true;
            labelChild.Location = new Point(729, 58);
            labelChild.Name = "labelChild";
            labelChild.Size = new Size(60, 25);
            labelChild.TabIndex = 2;
            labelChild.Text = "Fructe";
            // 
            // labelParent
            // 
            labelParent.Anchor = AnchorStyles.Bottom | AnchorStyles.Left;
            labelParent.AutoSize = true;
            labelParent.Location = new Point(178, 265);
            labelParent.Name = "labelParent";
            labelParent.Size = new Size(131, 25);
            labelParent.TabIndex = 3;
            labelParent.Text = "Tipuri de fructe";
            // 
            // numericUpDownPret
            // 
            numericUpDownPret.Location = new Point(157, 211);
            numericUpDownPret.Name = "numericUpDownPret";
            numericUpDownPret.Size = new Size(120, 31);
            numericUpDownPret.TabIndex = 4;
            // 
            // textBoxDenumire
            // 
            textBoxDenumire.Location = new Point(49, 90);
            textBoxDenumire.Name = "textBoxDenumire";
            textBoxDenumire.PlaceholderText = "Denumire";
            textBoxDenumire.Size = new Size(228, 31);
            textBoxDenumire.TabIndex = 5;
            // 
            // textBoxCuloare
            // 
            textBoxCuloare.Location = new Point(49, 127);
            textBoxCuloare.Name = "textBoxCuloare";
            textBoxCuloare.PlaceholderText = "Culoare";
            textBoxCuloare.Size = new Size(228, 31);
            textBoxCuloare.TabIndex = 6;
            // 
            // textBoxParentId
            // 
            textBoxParentId.Location = new Point(49, 52);
            textBoxParentId.Name = "textBoxParentId";
            textBoxParentId.PlaceholderText = "Id tip fruct";
            textBoxParentId.Size = new Size(150, 31);
            textBoxParentId.TabIndex = 7;
            // 
            // buttonAdd
            // 
            buttonAdd.Location = new Point(325, 65);
            buttonAdd.Name = "buttonAdd";
            buttonAdd.Size = new Size(112, 34);
            buttonAdd.TabIndex = 8;
            buttonAdd.Text = "Add";
            buttonAdd.UseVisualStyleBackColor = true;
            buttonAdd.Click += buttonAdd_Click;
            // 
            // buttonUpdate
            // 
            buttonUpdate.Location = new Point(325, 136);
            buttonUpdate.Name = "buttonUpdate";
            buttonUpdate.Size = new Size(112, 34);
            buttonUpdate.TabIndex = 9;
            buttonUpdate.Text = "Update";
            buttonUpdate.UseVisualStyleBackColor = true;
            buttonUpdate.Click += buttonUpdate_Click;
            // 
            // buttonDelete
            // 
            buttonDelete.Location = new Point(325, 204);
            buttonDelete.Name = "buttonDelete";
            buttonDelete.Size = new Size(112, 34);
            buttonDelete.TabIndex = 10;
            buttonDelete.Text = "Delete";
            buttonDelete.UseVisualStyleBackColor = true;
            buttonDelete.Click += buttonDelete_Click;
            // 
            // textBoxLunaOptima
            // 
            textBoxLunaOptima.Location = new Point(49, 164);
            textBoxLunaOptima.Name = "textBoxLunaOptima";
            textBoxLunaOptima.PlaceholderText = "Luna optima culegere";
            textBoxLunaOptima.Size = new Size(228, 31);
            textBoxLunaOptima.TabIndex = 11;
            // 
            // labelPretMediu
            // 
            labelPretMediu.Anchor = AnchorStyles.Bottom | AnchorStyles.Left;
            labelPretMediu.AutoSize = true;
            labelPretMediu.Location = new Point(49, 213);
            labelPretMediu.Name = "labelPretMediu";
            labelPretMediu.Size = new Size(102, 25);
            labelPretMediu.TabIndex = 12;
            labelPretMediu.Text = "Pret mediu:";
            // 
            // FormFructe
            // 
            AutoScaleDimensions = new SizeF(10F, 25F);
            AutoScaleMode = AutoScaleMode.Font;
            ClientSize = new Size(1114, 559);
            Controls.Add(labelPretMediu);
            Controls.Add(textBoxLunaOptima);
            Controls.Add(buttonDelete);
            Controls.Add(buttonUpdate);
            Controls.Add(buttonAdd);
            Controls.Add(textBoxParentId);
            Controls.Add(textBoxCuloare);
            Controls.Add(textBoxDenumire);
            Controls.Add(numericUpDownPret);
            Controls.Add(labelParent);
            Controls.Add(labelChild);
            Controls.Add(dataGridViewChild);
            Controls.Add(dataGridViewParent);
            Name = "FormFructe";
            Text = "Form1";
            Load += FormFructe_Load;
            ((System.ComponentModel.ISupportInitialize)dataGridViewParent).EndInit();
            ((System.ComponentModel.ISupportInitialize)dataGridViewChild).EndInit();
            ((System.ComponentModel.ISupportInitialize)numericUpDownPret).EndInit();
            ResumeLayout(false);
            PerformLayout();
        }

        #endregion

        private DataGridView dataGridViewParent;
        private DataGridView dataGridViewChild;
        private Label labelChild;
        private Label labelParent;
        private NumericUpDown numericUpDownPret;
        private TextBox textBoxDenumire;
        private TextBox textBoxCuloare;
        private TextBox textBoxParentId;
        private Button buttonAdd;
        private Button buttonUpdate;
        private Button buttonDelete;
        private TextBox textBoxLunaOptima;
        private Label labelPretMediu;
    }
}
