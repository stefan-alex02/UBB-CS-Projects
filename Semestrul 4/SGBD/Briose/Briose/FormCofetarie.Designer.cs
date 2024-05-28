namespace Briose;

partial class FormCofetarie {
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
        textBoxMarime = new TextBox();
        textBoxCuloare = new TextBox();
        textBoxParentId = new TextBox();
        buttonAdd = new Button();
        buttonUpdate = new Button();
        buttonDelete = new Button();
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
        dataGridViewParent.Location = new Point(49, 257);
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
        dataGridViewChild.Size = new Size(414, 392);
        dataGridViewChild.TabIndex = 1;
        dataGridViewChild.CellClick += dataGridViewChild_CellClick;
        // 
        // labelChild
        // 
        labelChild.Anchor = AnchorStyles.Top | AnchorStyles.Bottom | AnchorStyles.Left | AnchorStyles.Right;
        labelChild.AutoSize = true;
        labelChild.Location = new Point(660, 49);
        labelChild.Name = "labelChild";
        labelChild.Size = new Size(60, 25);
        labelChild.TabIndex = 2;
        labelChild.Text = "Briose";
        // 
        // labelParent
        // 
        labelParent.Anchor = AnchorStyles.Bottom | AnchorStyles.Left;
        labelParent.AutoSize = true;
        labelParent.Location = new Point(188, 219);
        labelParent.Name = "labelParent";
        labelParent.Size = new Size(78, 25);
        labelParent.TabIndex = 3;
        labelParent.Text = "Cofetarii";
        // 
        // numericUpDownPret
        // 
        numericUpDownPret.Location = new Point(72, 173);
        numericUpDownPret.Name = "numericUpDownPret";
        numericUpDownPret.Size = new Size(180, 31);
        numericUpDownPret.TabIndex = 4;
        // 
        // textBoxMarime
        // 
        textBoxMarime.Location = new Point(72, 125);
        textBoxMarime.Name = "textBoxMarime";
        textBoxMarime.PlaceholderText = "Marime";
        textBoxMarime.Size = new Size(150, 31);
        textBoxMarime.TabIndex = 5;
        // 
        // textBoxCuloare
        // 
        textBoxCuloare.Location = new Point(72, 88);
        textBoxCuloare.Name = "textBoxCuloare";
        textBoxCuloare.PlaceholderText = "Culoare";
        textBoxCuloare.Size = new Size(150, 31);
        textBoxCuloare.TabIndex = 6;
        // 
        // textBoxParentId
        // 
        textBoxParentId.Location = new Point(72, 51);
        textBoxParentId.Name = "textBoxParentId";
        textBoxParentId.PlaceholderText = "Id parinte";
        textBoxParentId.Size = new Size(150, 31);
        textBoxParentId.TabIndex = 7;
        // 
        // buttonAdd
        // 
        buttonAdd.Location = new Point(325, 49);
        buttonAdd.Name = "buttonAdd";
        buttonAdd.Size = new Size(112, 34);
        buttonAdd.TabIndex = 8;
        buttonAdd.Text = "Add";
        buttonAdd.UseVisualStyleBackColor = true;
        buttonAdd.Click += buttonAdd_Click;
        // 
        // buttonUpdate
        // 
        buttonUpdate.Location = new Point(325, 106);
        buttonUpdate.Name = "buttonUpdate";
        buttonUpdate.Size = new Size(112, 34);
        buttonUpdate.TabIndex = 9;
        buttonUpdate.Text = "Update";
        buttonUpdate.UseVisualStyleBackColor = true;
        buttonUpdate.Click += buttonUpdate_Click;
        // 
        // buttonDelete
        // 
        buttonDelete.Location = new Point(325, 170);
        buttonDelete.Name = "buttonDelete";
        buttonDelete.Size = new Size(112, 34);
        buttonDelete.TabIndex = 10;
        buttonDelete.Text = "Delete";
        buttonDelete.UseVisualStyleBackColor = true;
        buttonDelete.Click += buttonDelete_Click;
        // 
        // FormCofetarie
        // 
        AutoScaleDimensions = new SizeF(10F, 25F);
        AutoScaleMode = AutoScaleMode.Font;
        ClientSize = new Size(933, 523);
        Controls.Add(buttonDelete);
        Controls.Add(buttonUpdate);
        Controls.Add(buttonAdd);
        Controls.Add(textBoxParentId);
        Controls.Add(textBoxCuloare);
        Controls.Add(textBoxMarime);
        Controls.Add(numericUpDownPret);
        Controls.Add(labelParent);
        Controls.Add(labelChild);
        Controls.Add(dataGridViewChild);
        Controls.Add(dataGridViewParent);
        Name = "FormCofetarie";
        Text = "Form1";
        Load += FormCofetarie_Load;
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
    private TextBox textBoxMarime;
    private TextBox textBoxCuloare;
    private TextBox textBoxParentId;
    private Button buttonAdd;
    private Button buttonUpdate;
    private Button buttonDelete;
}